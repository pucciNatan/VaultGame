package com.vaultgame.customer.application.port.in;

import java.util.UUID;

public interface RemoveWishlistItemPort {
    void remove(UUID customerId, String productId);
}
