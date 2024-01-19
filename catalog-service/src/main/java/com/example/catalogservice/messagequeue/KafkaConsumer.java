package com.example.catalogservice.messagequeue;

import com.example.catalogservice.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(CatalogRepository catalogRepository, ObjectMapper objectMapper) {
        this.catalogRepository = catalogRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "${kafka.topic.order.catalog}")
    public void consumeOrderCatalogMessage(String kafkaMessage){
        log.info("kafaka Message: --> "+ kafkaMessage);
        try {
            Map<Object, Object> map = objectMapper.readValue(kafkaMessage, new TypeReference<>() {});
            catalogRepository.findByProductId((String) map.get("productId")).ifPresent(product -> {
                product.setStock(product.getStock() - (Integer) map.get("quantity"));
                product.setModeDate(new Date());
            });
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

}
