package com.vaultgame.inventory.domain.model;

import java.util.Objects;

public record ReservationLine(String productId, int quantity) {

    public ReservationLine {
        Objects.requireNonNull(productId, "productId");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
