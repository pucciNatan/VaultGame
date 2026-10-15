package com.vaultgame.inventory.domain.exception;

public class ProductInventoryNotFoundException extends RuntimeException {

    public ProductInventoryNotFoundException(String productId) {
        super("Inventory not found for product: " + productId);
    }
}
