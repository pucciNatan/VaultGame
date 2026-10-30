package com.vaultgame.customer.application.usecase.wishlist;

import com.vaultgame.customer.application.port.in.AddWishlistItemPort;
import com.vaultgame.customer.application.port.in.ListWishlistPort;
import com.vaultgame.customer.application.port.in.RemoveWishlistItemPort;
import com.vaultgame.customer.application.port.out.WishlistRepositoryPort;
import com.vaultgame.customer.domain.exception.WishlistItemAlreadyExistsException;
import com.vaultgame.customer.domain.exception.WishlistItemNotFoundException;
import com.vaultgame.customer.domain.model.wishlist.WishlistItem;
import java.util.List;
import java.util.UUID;

public class WishlistUseCase implements ListWishlistPort, AddWishlistItemPort, RemoveWishlistItemPort {

    private final WishlistRepositoryPort wishlistRepository;

    public WishlistUseCase(WishlistRepositoryPort wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<WishlistItem> execute(UUID customerId) {
        return wishlistRepository.findByCustomerId(customerId);
    }

    @Override
    public WishlistItem execute(UUID customerId, String productId) {
        String normalizedProductId = productId.trim();
        if (wishlistRepository.findByCustomerIdAndProductId(customerId, normalizedProductId).isPresent()) {
            throw new WishlistItemAlreadyExistsException(normalizedProductId);
        }
        WishlistItem item = WishlistItem.create(customerId, normalizedProductId);
        return wishlistRepository.save(item);
    }

    @Override
    public void remove(UUID customerId, String productId) {
        WishlistItem item = wishlistRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new WishlistItemNotFoundException(productId));
        wishlistRepository.delete(item);
    }
}
