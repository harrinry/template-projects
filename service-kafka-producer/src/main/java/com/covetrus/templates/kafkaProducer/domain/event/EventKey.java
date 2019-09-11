package com.covetrus.templates.kafkaProducer.domain.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventKey {
    public static String DEFAULT_DELIM = "|";

    // Event key fields
    private String partNames;
    private String partValues;
    private String delim;

}
