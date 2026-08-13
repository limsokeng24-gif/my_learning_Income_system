package com.school_management.overseas_language_centre.exception;

import com.school_management.overseas_language_centre.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseError.<String>builder()
                        .status(false)
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Resource not found")
                        .timestamp(LocalDateTime.now())
                        .errors(ex.getMessage())
                        .build()
        );
    }
}
