package com.vaultgame.order.domain.model.cart;

import java.util.UUID;

public final class CartKeys {

    private CartKeys() {
    }

    public static String guest(UUID cartId) {
        return "cart:guest:" + cartId;
    }

    public static String user(UUID customerId) {
        return "cart:user:" + customerId;
    }
}
