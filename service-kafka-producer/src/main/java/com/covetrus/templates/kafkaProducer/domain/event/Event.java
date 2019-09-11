package com.covetrus.templates.kafkaProducer.domain.event;

import lombok.Data;

@Data
public class Event {
    private EventContext context;
    private EventKey key;
    private Object object;
}
