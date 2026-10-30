package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.domain.model.wishlist.WishlistItem;
import java.util.List;
import java.util.UUID;

public interface ListWishlistPort {
    List<WishlistItem> execute(UUID customerId);
}
