package com.covetrus.templates.kafkaProducer.service;

import com.covetrus.templates.kafkaProducer.config.ApplicationProperties;
import com.covetrus.templates.kafkaProducer.domain.Order;
import com.covetrus.templates.kafkaProducer.domain.event.*;
import com.covetrus.templates.kafkaProducer.messaging.KafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    @Autowired private ApplicationProperties applicationProperties;
    @Autowired private KafkaMessageProducer kafkaMessageProducer;

    @Value("${spring.application.name}")
    private String applicationName;

    // Process an order
    public void processOrders(final List<Order> orderList) {
        for (Order order : orderList) {
            log.debug("Processing order {} ...", order.getOrderId());

            Event event = new Event();

            EventContext context = new EventContext();
            context.setPublisher(applicationName);
            context.setName(EventNameEnum.CREATED);
            context.setType(EventTypeEnum.ORDER);
            context.setVersion("1.0");
            context.setTraceId(UUID.randomUUID().toString());
            event.setContext(context);

            EventKey eventKey = new EventKey();
            eventKey.setPartNames("orderId");
            eventKey.setPartValues(String.valueOf(order.getOrderId()));
            event.setKey(eventKey);

            event.setObject(order);

            // Send message to Kafka (if configuration setup says so)
            if (applicationProperties.getKafkaProducer().isSendMessages()) {
                kafkaMessageProducer.sendMessage(applicationProperties.getKafkaProducer().getTopicName(), event);
            }

            log.debug("Order processed: [id={}, created={}]",
                    order.getOrderId(), order.getDateCreated().toString());
        }
    }

}
