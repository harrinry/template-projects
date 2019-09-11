package com.covetrus.templates.kafkaProducer.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Order {
    private long orderId;
    private String guid;
    private double amount;
    private ZonedDateTime dateCreated;
    private String createdBy;
    private OrderStatusEnum status;

    @JsonGetter
    public String getDateCreated() {
        return this.dateCreated.format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
