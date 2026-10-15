package com.vaultgame.order.domain.exception;

public class CatalogProductNotFoundException extends RuntimeException {

    public CatalogProductNotFoundException(String productId) {
        super("Product not found in catalog: " + productId);
    }
}
