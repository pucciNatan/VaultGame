package com.vaultgame.inventory.domain.model;

import java.time.Instant;
import java.util.Objects;

public record InventoryItem(
        String productId,
        int availableQuantity,
        long version,
        Instant updatedAt
) {

    public InventoryItem {
        Objects.requireNonNull(productId, "productId");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("availableQuantity cannot be negative");
        }
        Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public static InventoryItem create(String productId, int availableQuantity) {
        return new InventoryItem(productId, availableQuantity, 0L, Instant.now());
    }

    public InventoryItem withAvailableQuantity(int newQuantity, long newVersion) {
        return new InventoryItem(productId, newQuantity, newVersion, Instant.now());
    }
}
