package com.example.orderservice.messagequeue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/11
 * sinkConnector에 사용하기 위해서는 schema와 payload필드를 반드시 가져야한다.
 * 아니면 converter설정을 따로 적용해야함.
 */
@Data
@AllArgsConstructor
public class SinkConnectorMessage implements Serializable {
    private static final long serialVersionUID = -1836858289129271867L;

    private Schema schema;
    private Object payload;
}
