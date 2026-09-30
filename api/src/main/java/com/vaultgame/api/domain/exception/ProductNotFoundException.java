package com.vaultgame.api.domain.exception;

public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(String id) {
        super("Product not found: " + id);
    }
}
