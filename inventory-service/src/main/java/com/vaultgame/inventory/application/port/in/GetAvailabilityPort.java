package com.vaultgame.inventory.application.port.in;

import com.vaultgame.inventory.domain.model.InventoryItem;

import java.util.Collection;
import java.util.List;

public interface GetAvailabilityPort {

    InventoryItem getByProductId(String productId);

    List<InventoryItem> getByProductIds(Collection<String> productIds);
}
