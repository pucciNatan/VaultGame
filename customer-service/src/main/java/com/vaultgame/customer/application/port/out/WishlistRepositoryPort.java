package com.vaultgame.customer.application.port.out;

import com.vaultgame.customer.domain.model.wishlist.WishlistItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepositoryPort {

    WishlistItem save(WishlistItem item);

    List<WishlistItem> findByCustomerId(UUID customerId);

    Optional<WishlistItem> findByCustomerIdAndProductId(UUID customerId, String productId);

    void delete(WishlistItem item);
}
