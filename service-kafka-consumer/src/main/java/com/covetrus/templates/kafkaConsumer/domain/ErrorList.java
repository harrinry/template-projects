package com.covetrus.templates.kafkaConsumer.domain;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ErrorList {
    private Hashtable<String, ArrayList<String>> errors = new Hashtable<>();
    private int count = 0;

    public void addError(final String field, final String errorMessage) {
        ArrayList<String> msgs = errors.get(field);

        if (msgs == null) {
            msgs = new ArrayList<>();
            msgs.add(errorMessage);
            errors.put(field, msgs);
        } else {
            msgs.add(errorMessage);
        }

        count++;
    }

    public int getCount() {
        return count;
    }

    public Enumeration<String> getFieldList() {
        return errors.keys();
    }

    public List<String> getErrorMessagesForField(final String field) {
        ArrayList<String> msgs = errors.get(field);

        return msgs;
    }

    @Override
    public String toString() {
        // Return as Json.
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append(" \"errors\": [");

        Enumeration<String> fields = errors.keys();

        int i = 1;

        while (fields.hasMoreElements()) {
            String field = fields.nextElement();

            // format is:    { "field": "xxx", "msgs": ["message 1", "message 2"] }
            if (i > 1) result.append(",");
            result.append("{ ");

            result.append("\"field\": \"");
            result.append(field);
            result.append("\",");

            result.append("\"msgs\": [");

            ArrayList<String> msgs = errors.get(field);

            int j = 1;
            for (String errorMessage : msgs) {
                if (j > 1) result.append(",");
                result.append("\"");
                result.append(errorMessage);
                result.append("\"");
                j++;
            }

            result.append("]");
            result.append("}");

            i++;
        }

        result.append(" ]");
        result.append("}");

        return result.toString();
    }
}
