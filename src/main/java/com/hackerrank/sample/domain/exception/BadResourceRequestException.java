package com.hackerrank.sample.domain.exception;

public class BadResourceRequestException extends RuntimeException {
    public BadResourceRequestException(String message) {
        super(message);
    }
}
