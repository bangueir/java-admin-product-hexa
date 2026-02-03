package com.hackerrank.sample.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, String> response = new HashMap<>();

        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().isEmpty()
                    ? "request"
                    : invalidFormatException.getPath().get(0).getFieldName();
            response.put("error", "Invalid field format");
            response.put("message", "Invalid value for field '" + fieldName + "'.");
        } else {
            response.put("error", "Invalid JSON format");
            response.put("message", cause != null ? cause.getMessage() : ex.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { NoSuchResourceFoundException.class })
    public ResponseEntity<Object> handlEntityNotFoundException(NoSuchResourceFoundException noSuchResourceFoundException) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", noSuchResourceFoundException.getMessage());  
        response.put("status", HttpStatus.NOT_FOUND.value());  

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

}
