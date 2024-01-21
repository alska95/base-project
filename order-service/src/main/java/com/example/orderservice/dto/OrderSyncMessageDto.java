package com.example.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/12
 */
@Data
@NoArgsConstructor
public class OrderSyncMessageDto implements Serializable {
    private static final long serialVersionUID = 1829722361687863998L;
    private String order_id;
    private String product_id;
    private Integer quantity;
    private Integer total_price;
    private Integer unit_price;
    private String user_id;
}
