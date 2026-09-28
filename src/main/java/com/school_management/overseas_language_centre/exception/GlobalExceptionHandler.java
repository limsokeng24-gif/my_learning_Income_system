package com.school_management.overseas_language_centre.exception;

import com.school_management.overseas_language_centre.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // ==========================================
    // 1. Resource Not Found - 404
    // ==========================================
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
    // ==========================================
    // 2. Validation Error - 400
    // ==========================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseError.<Map<String, String>>builder()
                        .status(false)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .timestamp(LocalDateTime.now())
                        .errors(errors)
                        .build()
        );
    }

    // ==========================================
    // 3. Illegal Argument - 400
    // ==========================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseError.<String>builder()
                        .status(false)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Bad request")
                        .timestamp(LocalDateTime.now())
                        .errors(ex.getMessage())
                        .build()
        );
    }

}
