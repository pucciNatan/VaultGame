package com.vaultgame.api.infrastructure.web.exception;

import java.util.List;
import org.springframework.http.HttpStatus;

public final class ApiErrorFactory {

    private ApiErrorFactory() {}

    public static ApiErrorResponse build(HttpStatus status, String path, String message) {
        return build(status, path, message, null);
    }

    public static ApiErrorResponse build(
            HttpStatus status, String path, String message, List<ApiErrorResponse.FieldError> errors) {
        return new ApiErrorResponse(
                java.time.Instant.now(), status.value(), status.getReasonPhrase(), message, path, errors);
    }
}
