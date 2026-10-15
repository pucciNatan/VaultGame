package com.vaultgame.inventory.infrastructure.messaging;

import java.time.Instant;

/**
 * Contract for a future Kafka event on topic {@code vaultgame.inventory.adjusted}.
 */
public record InventoryAdjustedEvent(String productId, int availableQuantity, Instant adjustedAt) {
}
