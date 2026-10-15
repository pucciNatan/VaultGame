package com.vaultgame.inventory.domain.repository;

import com.vaultgame.inventory.domain.model.InventoryItem;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    Optional<InventoryItem> findByProductId(String productId);

    List<InventoryItem> findByProductIds(Collection<String> productIds);

    InventoryItem save(InventoryItem item);
}
