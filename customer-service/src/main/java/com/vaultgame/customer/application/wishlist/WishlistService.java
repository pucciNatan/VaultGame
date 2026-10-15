package com.vaultgame.customer.application.wishlist;

import com.vaultgame.customer.domain.wishlist.WishlistItem;
import com.vaultgame.customer.domain.wishlist.WishlistItemAlreadyExistsException;
import com.vaultgame.customer.domain.wishlist.WishlistItemNotFoundException;
import com.vaultgame.customer.domain.wishlist.WishlistRepository;

import java.util.List;
import java.util.UUID;

public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<WishlistItem> list(UUID customerId) {
        return wishlistRepository.findByCustomerId(customerId);
    }

    public WishlistItem add(UUID customerId, String productId) {
        String normalizedProductId = productId.trim();
        if (wishlistRepository.findByCustomerIdAndProductId(customerId, normalizedProductId).isPresent()) {
            throw new WishlistItemAlreadyExistsException(normalizedProductId);
        }
        WishlistItem item = WishlistItem.create(customerId, normalizedProductId);
        return wishlistRepository.save(item);
    }

    public void remove(UUID customerId, String productId) {
        WishlistItem item = wishlistRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new WishlistItemNotFoundException(productId));
        wishlistRepository.delete(item);
    }
}
