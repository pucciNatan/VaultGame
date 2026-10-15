package com.vaultgame.customer.domain.wishlist;

public class WishlistItemAlreadyExistsException extends RuntimeException {

    public WishlistItemAlreadyExistsException(String productId) {
        super("Product already in wishlist: " + productId);
    }
}
