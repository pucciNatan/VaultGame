package com.vaultgame.order.domain.cart;

public class MissingCartIdentityException extends RuntimeException {

    public MissingCartIdentityException() {
        super("Provide Authorization Bearer token or X-Cart-Id header");
    }
}
