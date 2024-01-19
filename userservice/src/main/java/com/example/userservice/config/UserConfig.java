package com.example.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


/**
 * Created by kyeongha55@linecorp.com on 2024/01/12
 */
@Configuration
public class UserConfig {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> getCircuitBreakerConfig() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(1) //100번 중 1번 실패하면 break, Default: 50
                .waitDurationInOpenState(Duration.ofMillis(1000)) //break됐으면 1초 후에 복구
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) //CircuitBreaker닫힐 때 로그 타입
                .slidingWindowSize(2) //슬라이딩 창 크기
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
