package com.covetrus.templates.kafkaConsumer.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Order {
    private long orderId;
    private String guid;
    private double amount;
    private Timestamp dateCreated;
    private String createdBy;
    private OrderStatusEnum status;

}
