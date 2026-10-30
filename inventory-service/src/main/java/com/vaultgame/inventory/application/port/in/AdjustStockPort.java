package com.vaultgame.inventory.application.port.in;

import com.vaultgame.inventory.domain.model.InventoryItem;

public interface AdjustStockPort {

    InventoryItem adjust(String productId, int availableQuantity);
}
