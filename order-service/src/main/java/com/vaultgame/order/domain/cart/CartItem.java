package com.vaultgame.order.domain.cart;

import java.util.Objects;

public record CartItem(String productId, int quantity) {

    public CartItem {
        Objects.requireNonNull(productId, "productId");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
