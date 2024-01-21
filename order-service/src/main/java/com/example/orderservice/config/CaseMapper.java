package com.example.orderservice.config;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by rhsalska55@naver.com on 2024/01/12
 *
 * camelCase > snakeCase
 * snakeCase > camelCase
 * 변환 가능
 *
 * 카멜 케이스를 스네이크 케이스로 매핑해주는 매퍼가 없는것 같아서 만듬.
 */
@Component
public class CaseMapper {

    public <T, R> R map(T source, Class<R> targetClass) {
        R result = instantiateTargetClass(targetClass);
        Arrays.stream(targetClass.getDeclaredFields())
                .filter(this::isNotStaticOrFinal)
                .forEach(resultField -> mapField(source, result, resultField));
        return result;
    }

    private <R> R instantiateTargetClass(Class<R> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create an instance of " + targetClass.getName() + ". Need default constructor.", e);
        }
    }

    private boolean isNotStaticOrFinal(Field field) {
        return !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
    }

    private <T, R> void mapField(T source, R result, Field resultField) {
        try {
            String targetFieldName = resultField.getName();
            String sourceFieldName = isCamelCase(targetFieldName) ? toSnakeCase(targetFieldName) : toCamelCase(targetFieldName);
            Field sourceField = source.getClass().getDeclaredField(sourceFieldName);
            sourceField.setAccessible(true);
            resultField.setAccessible(true);
            resultField.set(result, sourceField.get(source));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[CaseMapper] Error occurred while mapping case.", e);
        }
    }

    private boolean isCamelCase(String s) {
        return s.chars().anyMatch(Character::isUpperCase);
    }

    private String toSnakeCase(String s) {
        return Arrays.stream(s.split("(?=[A-Z])")) //대문자를 기준으로 문자열 분리
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"));
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_"); //_로 분리하고 첫 글자 대문자로
        return Arrays.stream(parts, 1, parts.length)
                .map(this::capitalize)
                .reduce(parts[0], String::concat);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
