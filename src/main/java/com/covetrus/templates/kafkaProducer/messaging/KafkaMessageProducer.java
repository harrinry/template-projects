package com.covetrus.templates.kafkaProducer.messaging;

import com.covetrus.templates.kafkaProducer.config.ApplicationProperties;
import com.covetrus.templates.kafkaProducer.domain.event.Event;
import com.covetrus.templates.kafkaProducer.domain.event.EventKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class KafkaMessageProducer {
    @Autowired private ApplicationProperties applicationProperties;
    @Autowired private KafkaTemplate<EventKey, Object> kafkaTemplate;

    public void sendMessage(final String topicName, final Event event) {
        Message<Object> message = MessageBuilder
                .withPayload(event.getObject())
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.MESSAGE_KEY, event.getKey())
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .setHeader(applicationProperties.getKafkaProducer().getHeaderNames().getPublisher(), event.getContext().getPublisher().getBytes())
                .setHeader(applicationProperties.getKafkaProducer().getHeaderNames().getType(), event.getContext().getType().toString().getBytes())
                .setHeader(applicationProperties.getKafkaProducer().getHeaderNames().getEvent(), event.getContext().getName().toString().getBytes())
                .setHeader(applicationProperties.getKafkaProducer().getHeaderNames().getTraceId(), event.getContext().getTraceId().getBytes())
                .setHeader(applicationProperties.getKafkaProducer().getHeaderNames().getVersion(), event.getContext().getVersion().getBytes())
                .build();

        ListenableFuture<SendResult<EventKey, Object>> future = kafkaTemplate.send(message);

        future.addCallback(new ListenableFutureCallback<SendResult<EventKey, Object>>() {
            @Override
            public void onSuccess(SendResult<EventKey, Object> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                //TODO: in reality, we would do something different here
                log.error("Unable to send message [{}] due to exception: {}", message, ex.getMessage());
            }
        });
    }

}
