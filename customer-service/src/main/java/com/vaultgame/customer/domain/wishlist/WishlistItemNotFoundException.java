package com.vaultgame.customer.domain.wishlist;

public class WishlistItemNotFoundException extends RuntimeException {

    public WishlistItemNotFoundException(String productId) {
        super("Wishlist item not found: " + productId);
    }
}
