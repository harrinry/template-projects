package com.covetrus.templates.kafkaConsumer.exception;

import com.covetrus.templates.kafkaConsumer.domain.ErrorList;
import lombok.Getter;

public class InvalidFieldsException extends RuntimeException {
    @Getter private ErrorList errorList = new ErrorList();

    public InvalidFieldsException() {
    }

    public void addError(final String field, final String errorMesage) {
        errorList.addError(field, errorMesage);
    }

    public int getErrorCount() {
        return errorList.getCount();
    }

    @Override
    public String toString() {
        return errorList.toString();
    }
}
