package com.vaultgame.api.infrastructure.web.exception;

import com.vaultgame.api.domain.exception.DomainException;
import com.vaultgame.api.domain.exception.DuplicateProductGtinException;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ProductNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorFactory.build(HttpStatus.NOT_FOUND, request.getRequestURI(), exception.getMessage()));
    }

    @ExceptionHandler(DuplicateProductGtinException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateGtin(
            DuplicateProductGtinException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorFactory.build(HttpStatus.CONFLICT, request.getRequestURI(), exception.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomain(DomainException exception, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(ApiErrorFactory.build(HttpStatus.BAD_REQUEST, request.getRequestURI(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<ApiErrorResponse.FieldError> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(ApiErrorFactory.build(
                        HttpStatus.BAD_REQUEST, request.getRequestURI(), "Validation failed", errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadable(
            HttpMessageNotReadableException exception, HttpServletRequest request) {
        String message = JsonErrorMessageResolver.resolve(exception);
        return ResponseEntity.badRequest()
                .body(ApiErrorFactory.build(HttpStatus.BAD_REQUEST, request.getRequestURI(), message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        String message = "Invalid value '%s' for parameter '%s'"
                .formatted(exception.getValue(), exception.getName());
        return ResponseEntity.badRequest()
                .body(ApiErrorFactory.build(HttpStatus.BAD_REQUEST, request.getRequestURI(), message));
    }
}
