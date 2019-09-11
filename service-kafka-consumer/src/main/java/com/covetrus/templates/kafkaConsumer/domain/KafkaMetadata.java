package com.covetrus.templates.kafkaConsumer.domain;

import lombok.Data;
import org.apache.kafka.common.record.TimestampType;

@Data
public class KafkaMetadata {
    // Standard Kafka metadata fields
    private String topic;
    private Integer partition;
    private Long offset;
    private TimestampType timestampType;
    private Long timestamp;
}
