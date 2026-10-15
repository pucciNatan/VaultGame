package com.vaultgame.customer.infrastructure.web.wishlist;

import com.vaultgame.customer.application.wishlist.WishlistService;
import com.vaultgame.customer.domain.wishlist.WishlistItem;
import com.vaultgame.customer.infrastructure.security.AuthenticatedCustomerId;
import com.vaultgame.customer.infrastructure.web.wishlist.WishlistDtos.AddWishlistRequest;
import com.vaultgame.customer.infrastructure.web.wishlist.WishlistDtos.WishlistItemResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers/me/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<WishlistItemResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return wishlistService.list(AuthenticatedCustomerId.from(jwt)).stream()
                .map(WishlistItemResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<WishlistItemResponse> add(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddWishlistRequest request
    ) {
        WishlistItem item = wishlistService.add(AuthenticatedCustomerId.from(jwt), request.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body(WishlistItemResponse.from(item));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@AuthenticationPrincipal Jwt jwt, @PathVariable String productId) {
        wishlistService.remove(AuthenticatedCustomerId.from(jwt), productId);
        return ResponseEntity.noContent().build();
    }
}
