package com.vaultgame.api.infrastructure.web.exception;

import com.vaultgame.api.domain.exception.DomainException;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException exception) {
        DomainException domain = findDomainException(exception);
        if (domain != null) {
            return handleDomain(domain);
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid request body"));
    }

    private static DomainException findDomainException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof DomainException domainException) {
                return domainException;
            }
            current = current.getCause();
        }
        return null;
    }
}
