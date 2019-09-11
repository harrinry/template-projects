package com.covetrus.templates.kafkaProducer.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FieldFormatHelper {
    public static ZonedDateTime getZonedDateTimeFromString(final String inputDate) {
        ZonedDateTime result = ZonedDateTime.parse(inputDate, DateTimeFormatter.ISO_DATE_TIME);

        return result;
    }
}
