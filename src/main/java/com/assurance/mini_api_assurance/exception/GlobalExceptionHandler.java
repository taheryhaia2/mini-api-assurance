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

        // On utilise une HashMap classique, qui accepte les valeurs null (contrairement à Map.of)
        Map<String, String> body = new HashMap<>();

        // Si le message est null, on met un texte par défaut, sinon on prend le vrai message
        String message = (ex.getMessage() != null) ? ex.getMessage() : "Une erreur inattendue s'est produite";
        body.put("erreur", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}