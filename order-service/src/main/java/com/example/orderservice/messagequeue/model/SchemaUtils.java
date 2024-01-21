package com.example.orderservice.messagequeue.model;

import java.util.Arrays;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/11
 */
public class SchemaUtils {
    /**
     * orders 테이블(OrderEntity)에 sinkConnector을 사용하여 데이터를 넣기 위한 스키마
     * Primitive Types: int8, int16, int32, int64, float32, float64, boolean, string, bytes
     * Complex Types: array, map, struct
     * type은 모두 소문자여야됌
     * */
    public static Schema ORDER_SYNC_SCHEMA = new Schema(
            "struct",
            Arrays.asList(
                    new Field("order_id", "string", false),
                    new Field("product_id", "string", false),
                    new Field("quantity", "int32", false),
                    new Field("total_price", "int64", false),
                    new Field("unit_price", "int32", false),
                    new Field("user_id", "string", false)),
            false,
            "orders");
}
