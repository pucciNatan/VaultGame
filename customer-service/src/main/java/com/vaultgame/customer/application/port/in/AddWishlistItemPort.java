package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.domain.model.wishlist.WishlistItem;
import java.util.UUID;

public interface AddWishlistItemPort {
    WishlistItem execute(UUID customerId, String productId);
}
