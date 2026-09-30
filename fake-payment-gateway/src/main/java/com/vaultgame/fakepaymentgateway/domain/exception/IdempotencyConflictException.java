package com.vaultgame.fakepaymentgateway.domain.exception;

public class IdempotencyConflictException extends RuntimeException {

    public IdempotencyConflictException(String idempotencyKey) {
        super("Idempotency key already used with a different request: " + idempotencyKey);
    }
}
