package com.example.orderservice.service;


import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    List<OrderEntity> getOrderListByUserId(String userId);
    Iterable<OrderEntity> getAllOrders();
}
