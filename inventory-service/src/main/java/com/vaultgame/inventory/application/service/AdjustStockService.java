package com.vaultgame.inventory.application.service;

import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.domain.repository.InventoryRepository;

public class AdjustStockService {

    private final InventoryRepository inventoryRepository;

    public AdjustStockService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryItem adjust(AdjustStockCommand command) {
        if (command.availableQuantity() < 0) {
            throw new IllegalArgumentException("availableQuantity cannot be negative");
        }
        return inventoryRepository.findByProductId(command.productId())
                .map(item -> inventoryRepository.save(
                        item.withAvailableQuantity(command.availableQuantity(), item.version())))
                .orElseGet(() -> inventoryRepository.save(
                        InventoryItem.create(command.productId(), command.availableQuantity())));
    }
}
