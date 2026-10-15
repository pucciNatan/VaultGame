package com.vaultgame.inventory.application.service;

import com.vaultgame.inventory.domain.exception.ProductInventoryNotFoundException;
import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.domain.repository.InventoryRepository;

import java.util.Collection;
import java.util.List;

public class GetAvailabilityService {

    private final InventoryRepository inventoryRepository;

    public GetAvailabilityService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryItem getByProductId(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductInventoryNotFoundException(productId));
    }

    public List<InventoryItem> getByProductIds(Collection<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }
        return inventoryRepository.findByProductIds(productIds);
    }
}
