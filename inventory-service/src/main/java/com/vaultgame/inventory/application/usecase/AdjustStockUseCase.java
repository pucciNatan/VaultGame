package com.vaultgame.inventory.application.usecase;

import com.vaultgame.inventory.application.port.in.AdjustStockPort;
import com.vaultgame.inventory.application.port.out.InventoryRepositoryPort;
import com.vaultgame.inventory.domain.model.InventoryItem;

public class AdjustStockUseCase implements AdjustStockPort {

    private final InventoryRepositoryPort inventoryRepository;

    public AdjustStockUseCase(InventoryRepositoryPort inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryItem adjust(String productId, int availableQuantity) {
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("availableQuantity cannot be negative");
        }
        return inventoryRepository.findByProductId(productId)
                .map(item -> inventoryRepository.save(
                        item.withAvailableQuantity(availableQuantity, item.version())))
                .orElseGet(() -> inventoryRepository.save(
                        InventoryItem.create(productId, availableQuantity)));
    }
}
