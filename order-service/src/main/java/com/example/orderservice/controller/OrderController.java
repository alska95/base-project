package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducerService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
//@RequestMapping("/order-service") //게이트웨이에서 붙는 prefix
@RequestMapping("") //게이트웨이에 segment적용
@Timed(value = "order", longTask = true)
public class OrderController {
    final OrderService orderService;
    final KafkaProducerService kafkaProducerService;
    final ModelMapper modelMapper;

    public OrderController(OrderService orderService, KafkaProducerService kafkaProducerService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.kafkaProducerService = kafkaProducerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/health-check")
    public String status(HttpServletRequest request){
        return String.format("It's working in Order Service on Port %s", request.getLocalPort());
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder order, @PathVariable String userId){
        OrderDto orderDto = modelMapper.map(order , OrderDto.class);
        orderDto.setUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(modelMapper.map(orderService.createOrder(orderDto), ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrderListByUserId(userId).stream()
                        .map(result -> modelMapper.map(result, ResponseOrder.class))
                        .collect(Collectors.toList()));
    }
}
