package com.jobtracker.backend.repository.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 2) // Order 3
public class RepositoryExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseExceptions(DataAccessException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Database Conflict");
        error.put("message", "A database error occurred. This could be due to stale data or constraint violations.");
        // Log the actual exception privately here in a real app: log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409
    }
}
