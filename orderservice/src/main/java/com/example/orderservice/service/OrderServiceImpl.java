package com.example.orderservice.service;


import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducerService;
import com.example.orderservice.repository.OrderRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ModelMapper modelMapper;


    public OrderServiceImpl(OrderRepository orderRepository, KafkaProducerService kafkaProducerService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQuantity() * orderDto.getUnitPrice());

        OrderEntity orderEntity = modelMapper.map(orderDto , OrderEntity.class);
        orderEntity.setCreatedAt(new Date());
        orderRepository.save(orderEntity);
        kafkaProducerService.produceOrderCatalogMessage(orderDto);
//        kafkaProducerService.produceOrderDbSyncMessage(orderDto);
        return modelMapper.map(orderEntity, OrderDto.class);
    }

    @Override
    public List<OrderEntity> getOrderListByUserId(String userId) {
        log.info("[OrderService] Before search order list"); //sleuth 사용 위해서 로깅. 나중에 AOP이용해서 처리해도 괜찮을듯
        List<OrderEntity> resultList = StreamSupport.stream(orderRepository.findByUserId(userId).spliterator(), false).collect(Collectors.toList());
        log.info("[OrderService] After search order list");
        return resultList;
    }

    @Override
    public Iterable<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
