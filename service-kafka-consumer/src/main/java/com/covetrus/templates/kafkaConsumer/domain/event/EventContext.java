package com.covetrus.templates.kafkaConsumer.domain.event;

import lombok.Data;

@Data
public class EventContext {
    private String publisher;
    private EventNameEnum name;
    private EventTypeEnum type;
    private String version;
    private String traceId;

}
