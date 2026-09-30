package com.vaultgame.customer.infrastructure.web.wishlist;

import com.vaultgame.customer.domain.wishlist.WishlistItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class WishlistDtos {

    private WishlistDtos() {
    }

    public record AddWishlistRequest(@NotBlank @Size(max = 64) String productId) {
    }

    public record WishlistItemResponse(String productId, Instant addedAt) {
        public static WishlistItemResponse from(WishlistItem item) {
            return new WishlistItemResponse(item.productId(), item.addedAt());
        }
    }
}
