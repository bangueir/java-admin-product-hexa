package com.hackerrank.sample.domain.exception;

public class NoSuchResourceFoundException extends RuntimeException {
    public NoSuchResourceFoundException(String message) {
        super(message);
    }
}
