package com.finflow.demo.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {

        System.out.println("❌ ERROR: " + ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}