package com.vaultgame.order.domain.cart;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(String productId) {
        super("Cart item not found: " + productId);
    }
}
