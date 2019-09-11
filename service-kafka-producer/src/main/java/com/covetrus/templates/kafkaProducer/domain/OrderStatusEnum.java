package com.covetrus.templates.kafkaProducer.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatusEnum {
    CREATED("C", "Created"),
    VERIFIED("V", "Verified"),
    SHIPPED("S", "Shipped");

    private String value;
    private String label;

    private OrderStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonCreator
    public static OrderStatusEnum create(String value) {
        String error = "Invalid Order Status (" + value + ") specified.";

        if (value == null) {
            throw new IllegalArgumentException(error);
        }

        for (OrderStatusEnum theEnum : values()) {
            if (value.equals(theEnum.value())) {
                return theEnum;
            }
        }

        throw new IllegalArgumentException(error);
    }

    @Override
    public String toString() {
        return label;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public String label() {
        return label;
    }
}
