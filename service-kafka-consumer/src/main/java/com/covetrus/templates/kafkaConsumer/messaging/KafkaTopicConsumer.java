package com.covetrus.templates.kafkaConsumer.messaging;

import com.covetrus.templates.kafkaConsumer.config.ApplicationProperties;
import com.covetrus.templates.kafkaConsumer.domain.ErrorList;
import com.covetrus.templates.kafkaConsumer.domain.KafkaMetadata;
import com.covetrus.templates.kafkaConsumer.domain.Order;
import com.covetrus.templates.kafkaConsumer.domain.event.Event;
import com.covetrus.templates.kafkaConsumer.domain.event.EventContext;
import com.covetrus.templates.kafkaConsumer.domain.event.EventKey;
import com.covetrus.templates.kafkaConsumer.exception.InvalidFieldsException;
import com.covetrus.templates.kafkaConsumer.service.OrderService;
import com.covetrus.templates.kafkaConsumer.util.JsonHelper;
import com.covetrus.templates.kafkaConsumer.util.KafkaHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.List;

@Service
@Slf4j
public class KafkaTopicConsumer {
    @Autowired private ApplicationProperties applicationProperties;
    @Autowired private OrderService orderService;

    @KafkaListener(topics = "${application.kafkaConsumer.topicName}", groupId = "${spring.kafka.consumer.group-id}", autoStartup="${application.kafkaConsumer.listenerEnabled}")
    public void consume(ConsumerRecord<String, String> cr, Acknowledgment ack) {

        // Get the standard Kafka metadata fields
        KafkaMetadata kafkaMetadata = KafkaHelper.getMetadataFields(cr);

        // Populate the event context fields from the Kafka header
        EventContext context = KafkaHelper.getContextFields(applicationProperties, cr.headers());

        // Get the Kafka message's key and value data
        EventKey eventKey = KafkaHelper.getEventKeyFields(cr.key());
        final String value = cr.value();
        Order order = JsonHelper.fromJson(value, Order.class);

        Event event = new Event();
        event.setContext(context);
        event.setKey(eventKey);
        event.setObject(order);

        try {
            // Process the message
            orderService.processEvent(kafkaMetadata, event);
            ack.acknowledge(); // we've processed the message, so the latest offset for this consumer should be recorded in the Kafka broker
            log.debug("--> message processed ok");
        } catch (Exception e) {
            // This exception can be trapped if the event processing validates each field of the message and rejects certain fields.
            if (e instanceof InvalidFieldsException) {
                // log each error for each field that is stored on the exception.
                InvalidFieldsException ife = (InvalidFieldsException) e;
                ErrorList errorList = ife.getErrorList();
                Enumeration<String> fieldList = errorList.getFieldList();
                log.error("InvalidFieldsException: ({} errors):", errorList.getCount());
                while (fieldList.hasMoreElements()) {
                    String field = fieldList.nextElement();
                    List<String> errorMessages = errorList.getErrorMessagesForField(field);
                    for (String errorMessage : errorMessages) {
                        log.error("  {}: {}", field, errorMessage);
                    }
                }
            } else {
                log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            }
            throw e; // by throwing an exception, the latest offset for this consumer does not get recorded in the Kafka broker
        }
    }
}
