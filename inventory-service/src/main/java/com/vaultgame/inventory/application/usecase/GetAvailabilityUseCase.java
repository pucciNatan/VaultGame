package com.vaultgame.inventory.application.usecase;

import com.vaultgame.inventory.application.port.in.GetAvailabilityPort;
import com.vaultgame.inventory.application.port.out.InventoryRepositoryPort;
import com.vaultgame.inventory.domain.exception.ProductInventoryNotFoundException;
import com.vaultgame.inventory.domain.model.InventoryItem;

import java.util.Collection;
import java.util.List;

public class GetAvailabilityUseCase implements GetAvailabilityPort {

    private final InventoryRepositoryPort inventoryRepository;

    public GetAvailabilityUseCase(InventoryRepositoryPort inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryItem getByProductId(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductInventoryNotFoundException(productId));
    }

    @Override
    public List<InventoryItem> getByProductIds(Collection<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }
        return inventoryRepository.findByProductIds(productIds);
    }
}
