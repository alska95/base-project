package com.example.orderservice.messagequeue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/11
 * 모두 반드시 필요한 필드임
 */
@Data
@AllArgsConstructor
public class Schema {
    private String type;
    private List<Field> fields; //fieldList로 하면 안됨.
    private boolean optional;
    private String name;
}
