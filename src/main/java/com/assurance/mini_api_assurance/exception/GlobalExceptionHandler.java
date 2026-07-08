package com.assurance.mini_api_assurance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        // Use a classic HashMap, which accepts null values (unlike Map.of)
        Map<String, String> body = new HashMap<>();

        // If the message is null, use a default text, otherwise use the real message
        String message = (ex.getMessage() != null) ? ex.getMessage() : "An unexpected error occurred";
        body.put("error", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
