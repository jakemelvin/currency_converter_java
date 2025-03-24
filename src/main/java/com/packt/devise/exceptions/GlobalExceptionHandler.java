package com.packt.devise.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.packt.devise.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest()
            .body(new ApiResponse("Erreur de validation", errors));
    }

    @ExceptionHandler({InvalidCurrencyException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiResponse> handleBadRequestExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest()
            .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(ApiConnectionException.class)
    public ResponseEntity<ApiResponse> handleApiConnectionException(ApiConnectionException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
            .body(new ApiResponse("Une erreur interne est survenue", null));
    }
}
