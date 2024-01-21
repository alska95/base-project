package com.example.orderservice.messagequeue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/11
 */
@Data
@AllArgsConstructor
public class Field {
    private String field;
    private String type;
    private boolean optional;
}
