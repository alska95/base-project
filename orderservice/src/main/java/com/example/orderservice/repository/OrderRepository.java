package com.example.orderservice.repository;

import com.example.orderservice.domain.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    Iterable<OrderEntity> findByUserId(String userId);
    //order 테이블의 id를 조회해서 찾아준다.
}
