package com.vaultgame.order.domain.exception;

public class InactiveProductException extends RuntimeException {

    public InactiveProductException(String productId) {
        super("Product is not active: " + productId);
    }
}
