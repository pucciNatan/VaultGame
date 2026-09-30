package com.vaultgame.customer.domain.wishlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository {

    WishlistItem save(WishlistItem item);

    List<WishlistItem> findByCustomerId(UUID customerId);

    Optional<WishlistItem> findByCustomerIdAndProductId(UUID customerId, String productId);

    void delete(WishlistItem item);
}
