package com.jobtracker.backend.controller.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE) // Order 4
public class ServerExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", "Internal Server Error");
		error.put("message", "An unexpected error occurred. Please contact support.");
		// log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR); // 500
	}
}
