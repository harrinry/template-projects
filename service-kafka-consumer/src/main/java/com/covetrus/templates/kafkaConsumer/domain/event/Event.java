package com.covetrus.templates.kafkaConsumer.domain.event;

import lombok.Data;

@Data
public class Event {
    private EventContext context;
    private EventKey key;
    private Object object;
}
