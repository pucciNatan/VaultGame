package com.vaultgame.order.adapter.out.messaging;

import java.time.Instant;
import java.util.UUID;

public record OrderPaidMessage(UUID eventId, Instant occurredAt, OrderPaidPayload payload) {

    public static OrderPaidMessage create(
            UUID orderId,
            UUID customerId,
            String email,
            String fullName,
            java.math.BigDecimal total,
            String currency
    ) {
        return new OrderPaidMessage(
                UUID.randomUUID(),
                Instant.now(),
                new OrderPaidPayload(orderId, customerId, email, fullName, total, currency)
        );
    }
}
