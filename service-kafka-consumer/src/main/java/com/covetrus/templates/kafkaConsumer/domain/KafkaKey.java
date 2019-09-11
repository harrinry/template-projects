package com.covetrus.templates.kafkaConsumer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/*
  A class that maps to the 'key' field in the Kafka message.  This is only used in the code that processes Kafka messages.
 */
public class KafkaKey {
    private static final String TIME_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Getter @Setter private String application;
    @Getter @Setter private String entityKey;
    @Getter @Setter private String entityType;
    @Getter @Setter private String modifiedBy;
    @Setter private Date dateModified = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT_ISO_8601)
    public Date getDateModified() {
        return this.dateModified;
    }

}
