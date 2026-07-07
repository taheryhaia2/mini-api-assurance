package com.assurance.mini_api_assurance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // On intercepte toutes les RuntimeException de l'application
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        // On crée un corps de réponse JSON propre avec le message d'erreur
        Map<String, String> body = Map.of("erreur", ex.getMessage());

        // On renvoie un statut 400 Bad Request au lieu de 500
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}