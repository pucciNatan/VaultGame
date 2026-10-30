package com.vaultgame.fakepaymentgateway.application.usecase;

public class DuplicateIdempotencyKeyException extends RuntimeException {

    public DuplicateIdempotencyKeyException(Throwable cause) {
        super("Duplicate idempotency key", cause);
    }
}
