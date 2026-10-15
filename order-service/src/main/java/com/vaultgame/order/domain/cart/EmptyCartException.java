package com.vaultgame.order.domain.cart;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Cart is empty");
    }
}
