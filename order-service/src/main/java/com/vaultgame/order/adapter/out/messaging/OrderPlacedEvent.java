package com.vaultgame.order.adapter.out.messaging;

/**
 * Kafka event on topic {@code vaultgame.order.paid} — see {@link OrderPaidMessage}.
 */
public record OrderPlacedEvent(java.util.UUID orderId, java.util.UUID customerId, java.time.Instant placedAt) {
}
