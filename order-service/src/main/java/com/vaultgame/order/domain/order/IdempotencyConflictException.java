package com.vaultgame.order.domain.order;

public class IdempotencyConflictException extends RuntimeException {

    public IdempotencyConflictException(String idempotencyKey) {
        super("Idempotency key reused with different payload: " + idempotencyKey);
    }
}
