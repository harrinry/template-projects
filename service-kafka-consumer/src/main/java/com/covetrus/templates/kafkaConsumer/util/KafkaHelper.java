package com.covetrus.templates.kafkaConsumer.util;

import com.covetrus.templates.kafkaConsumer.config.ApplicationProperties;
import com.covetrus.templates.kafkaConsumer.domain.KafkaMetadata;
import com.covetrus.templates.kafkaConsumer.domain.event.EventContext;
import com.covetrus.templates.kafkaConsumer.domain.event.EventKey;
import com.covetrus.templates.kafkaConsumer.domain.event.EventNameEnum;
import com.covetrus.templates.kafkaConsumer.domain.event.EventTypeEnum;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

public class KafkaHelper {
    public static EventKey getEventKeyFields(final String key) {
        EventKey eventKey = JsonHelper.fromJson(key, EventKey.class);

        return eventKey;
    }

    public static KafkaMetadata getMetadataFields(ConsumerRecord<String, String> cr) {
        KafkaMetadata metadata = new KafkaMetadata();

        metadata.setPartition(cr.partition());
        metadata.setOffset(cr.offset());
        metadata.setTopic(cr.topic());
        metadata.setTimestampType(cr.timestampType());
        metadata.setTimestamp(cr.timestamp());

        return metadata;
    }

    //TODO: probably could do this in a more elegant way
    public static EventContext getContextFields(final ApplicationProperties applicationProperties, final Headers headers) {
        EventContext context = new EventContext();

        headers.forEach(header -> {
            if (applicationProperties.getKafkaConsumer().getHeaderNames().getPublisher().equals(header.key())) {
                if (header.value() != null) context.setPublisher(new String(header.value()));
            } else if (applicationProperties.getKafkaConsumer().getHeaderNames().getEvent().equals(header.key())) {
                if (header.value() != null) context.setName(EventNameEnum.valueOf(new String(header.value())));
            } else if (applicationProperties.getKafkaConsumer().getHeaderNames().getType().equals(header.key())) {
                if (header.value() != null) context.setType(EventTypeEnum.valueOf(new String(header.value())));
            } else if (applicationProperties.getKafkaConsumer().getHeaderNames().getVersion().equals(header.key())) {
                if (header.value() != null) context.setVersion(new String(header.value()));
            } else if (applicationProperties.getKafkaConsumer().getHeaderNames().getTraceId().equals(header.key())) {
                if (header.value() != null) context.setTraceId(new String(header.value()));
            }
        });

        return context;
    }
}
