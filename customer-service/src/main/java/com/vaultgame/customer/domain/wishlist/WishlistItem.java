package com.vaultgame.customer.domain.wishlist;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record WishlistItem(UUID customerId, String productId, Instant addedAt) {

    public static WishlistItem create(UUID customerId, String productId) {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(productId, "productId");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        return new WishlistItem(customerId, productId.trim(), Instant.now());
    }

    public static WishlistItem rehydrate(UUID customerId, String productId, Instant addedAt) {
        return new WishlistItem(customerId, productId, addedAt);
    }
}
