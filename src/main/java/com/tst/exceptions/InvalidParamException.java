package com.tst.exceptions;

public class InvalidParamException extends RuntimeException {
    public InvalidParamException(String message) {
        super(message);
    }
}
