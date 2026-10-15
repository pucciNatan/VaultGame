package com.vaultgame.inventory.application.service;

public record AdjustStockCommand(String productId, int availableQuantity) {
}
