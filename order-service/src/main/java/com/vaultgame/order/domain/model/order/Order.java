package com.vaultgame.order.domain.model.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Order(
        UUID id,
        UUID customerId,
        String cartKey,
        OrderStatus status,
        String currency,
        BigDecimal totalAmount,
        String paymentTransactionId,
        UUID reservationId,
        UUID shippingAddressId,
        String idempotencyKey,
        String requestHash,
        List<OrderLine> lines,
        Instant createdAt,
        Instant updatedAt
) {

    public Order {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(cartKey, "cartKey");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(totalAmount, "totalAmount");
        Objects.requireNonNull(shippingAddressId, "shippingAddressId");
        Objects.requireNonNull(lines, "lines");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(updatedAt, "updatedAt");
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be empty");
        }
    }

    public static Order createPendingPayment(
            UUID id,
            UUID customerId,
            String cartKey,
            String currency,
            BigDecimal totalAmount,
            UUID shippingAddressId,
            String idempotencyKey,
            String requestHash,
            List<OrderLine> lines,
            UUID reservationId
    ) {
        Instant now = Instant.now();
        return new Order(
                id,
                customerId,
                cartKey,
                OrderStatus.PENDING_PAYMENT,
                currency,
                totalAmount,
                null,
                reservationId,
                shippingAddressId,
                idempotencyKey,
                requestHash,
                lines,
                now,
                now
        );
    }

    public Order withStatus(OrderStatus newStatus) {
        return new Order(
                id, customerId, cartKey, newStatus, currency, totalAmount,
                paymentTransactionId, reservationId, shippingAddressId,
                idempotencyKey, requestHash, lines, createdAt, Instant.now()
        );
    }

    public Order withPayment(String transactionId, OrderStatus newStatus) {
        return new Order(
                id, customerId, cartKey, newStatus, currency, totalAmount,
                transactionId, reservationId, shippingAddressId,
                idempotencyKey, requestHash, lines, createdAt, Instant.now()
        );
    }
}
