package com.simonatb.featureflag.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeatureFlagNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(FeatureFlagNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateFeatureFlagNameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateFeatureFlagNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of(
                "status", 400,
                "error", "Validation failed",
                "messages", errors,
                "timestamp", Instant.now().toString()
            )
        );
    }

    private Map<String, Object> errorBody(String message) {
        return Map.of(
            "status", 404,
            "error", message,
            "timestamp", Instant.now().toString()
        );
    }

}
