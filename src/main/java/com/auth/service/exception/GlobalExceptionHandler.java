package com.auth.service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling various types of exceptions across the application.
 * This class uses @ControllerAdvice to apply to all controllers globally and
 * provides centralized error handling for:
 * - Custom application exceptions
 * - Validation errors (DTO level)
 * - Database constraint violations
 * - Any uncaught exceptions
 * Ensures consistent error response structure for the client.
 * @author Mahendra
 * @since 2025
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom application-defined exceptions.
     *
     * @param ex the custom exception
     * @return a BAD_REQUEST (400) response with error message
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DTO-level validation errors.
     *
     * @param ex the MethodArgumentNotValidException
     * @return a BAD_REQUEST (400) response with field-specific validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles uncaught exceptions.
     *
     * @param ex the exception
     * @return INTERNAL_SERVER_ERROR (500) with error message
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles data integrity violations like duplicate key or unique constraint failures.
     *
     * @param ex the DataIntegrityViolationException
     * @return a CONFLICT (409) response with root cause details
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Duplicate entry or constraint violation");
        response.put("details", ex.getRootCause().getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles Hibernate-level constraint violations.
     *
     * @param ex the ConstraintViolationException
     * @return BAD_REQUEST (400) response with validation failure message
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed at DB level");
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
