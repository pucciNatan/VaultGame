package com.vaultgame.order.adapter.out.messaging;

import java.time.Instant;
import java.util.UUID;

/**
 * Contract for a future Kafka event on topic {@code vaultgame.order.placed}.
 */
public record OrderPlacedEvent(UUID orderId, UUID customerId, Instant placedAt) {
}
