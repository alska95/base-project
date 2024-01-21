package com.example.orderservice.messagequeue;

import com.example.orderservice.config.CaseMapper;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderSyncMessageDto;
import com.example.orderservice.messagequeue.model.SchemaUtils;
import com.example.orderservice.messagequeue.model.SinkConnectorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CaseMapper caseMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, CaseMapper caseMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.caseMapper = caseMapper;
    }

    @Value("${kafka.topic.order.catalog}")
    private String orderCatalogTopic;
    @Value("${kafka.topic.order.sync}")
    private String orderSyncTopic;

    private Object produceOrderMessage(String topic, Object messageObject) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(messageObject));
            log.info("Kafka Producer sent data from the Order microService" + messageObject);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return messageObject;
    }

    public void produceOrderCatalogMessage(OrderDto orderDto) {
        produceOrderMessage(orderCatalogTopic, orderDto);
    }

    /**
     * 예제 차원에서 sinkConnector을 사용하여 db에 order을 집어넣도록해봤다.
     *
     * 예외 처리가 불가능하여
     * order이 중복으로 들어오는 경우 예외를 던질 수 없어 catalog 상품의 개수가 0개여도 주문이 들어갈 수 있다. (어짜피 상품 개수 조회하는 로직이 없긴하지만 실제 상황가정)
     * 트랜잭션 처리도 불가능하여
     * 같은 트랜잭션 단위로 처리되어야 하는 작업이 있어도 롤백이 불가능하다.
     * 비동기적으로 처리하는게 핵심인 카프카에서 위 문제를 해결하기 위해 sinkConnector의 처리가 끝날때 까지 기다리고,
     * 응답을 받은 후에 롤백을 하거나 커밋을 하는것도 매우 비효율적인것 같다.
     *
     * 실제 환경에서 DB의 커넥션 풀이 감당하기 어려운 수준의 대량의 동시 요청이 들어오는 경우에
     * raceCondition에 대한 고려나 트랜잭션이 필요하지 않다면 사용할만 한 것 같다.
     * 부하가 집중되는 상황에서 카프카가 버퍼 역할을 수행하여 서비스에서 생성된 데이터를 임시로 저장하고 DB가 처리할 수 있을 때까지 대기 시켜서 부하 분산이 가능하다.
     * */
    public void produceOrderDbSyncMessage(OrderDto orderDto) {
        produceOrderMessage(orderSyncTopic, new SinkConnectorMessage(SchemaUtils.ORDER_SYNC_SCHEMA, caseMapper.map(orderDto, OrderSyncMessageDto.class)));
    }
}
