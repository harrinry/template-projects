package com.covetrus.templates.kafkaConsumer.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonHelper {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T fromJson(final String json, Class<T> objectType) {
        try {
            return objectMapper.readValue(json, objectType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(final Object object, Class<T> objectType) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return fromJson(json, objectType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJsonWithinContainer(final String json, final String containerName) {
        if (StringUtils.isEmpty(json)) {
            return "{}";
        } else {
            int pIndex = json.indexOf("\"" + containerName + "\"");

            if (pIndex != -1) {
                int start = json.indexOf('{', pIndex);

                if (start != -1) {
                    int last = json.length();
                    int end = json.lastIndexOf('}', last-2); // skip trailing '}' character
                    if (end != -1) {
                        return json.substring(start, end) + "}";
                    }
                }
            }

            return "{}";
        }
    }

}
