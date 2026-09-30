package com.vaultgame.fakepaymentgateway.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Payment(
        String transactionId,
        String reference,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        String idempotencyKey,
        String requestHash,
        Instant createdAt,
        Instant updatedAt
) {

    public static Payment create(
            String reference,
            BigDecimal amount,
            String currency,
            PaymentStatus status,
            String idempotencyKey,
            String requestHash
    ) {
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(requestHash, "requestHash");

        if (reference.isBlank()) {
            throw new IllegalArgumentException("reference must not be blank");
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }

        Instant now = Instant.now();
        return new Payment(
                "tx-" + UUID.randomUUID(),
                reference,
                amount,
                currency,
                status,
                idempotencyKey,
                requestHash,
                now,
                now
        );
    }

    public static Payment rehydrate(
            String transactionId,
            String reference,
            BigDecimal amount,
            String currency,
            PaymentStatus status,
            String idempotencyKey,
            String requestHash,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Payment(
                transactionId,
                reference,
                amount,
                currency,
                status,
                idempotencyKey,
                requestHash,
                createdAt,
                updatedAt
        );
    }
}
