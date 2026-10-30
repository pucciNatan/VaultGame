package com.vaultgame.customer.adapter.in.web.wishlist;

import com.vaultgame.customer.adapter.in.web.security.AuthenticatedCustomerId;
import com.vaultgame.customer.adapter.in.web.wishlist.WishlistDtos.AddWishlistRequest;
import com.vaultgame.customer.adapter.in.web.wishlist.WishlistDtos.WishlistItemResponse;
import com.vaultgame.customer.application.port.in.AddWishlistItemPort;
import com.vaultgame.customer.application.port.in.ListWishlistPort;
import com.vaultgame.customer.application.port.in.RemoveWishlistItemPort;
import com.vaultgame.customer.domain.model.wishlist.WishlistItem;
import jakarta.validation.Valid;
import java.util.List;
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

@RestController
@RequestMapping("/customers/me/wishlist")
public class WishlistController {

    private final ListWishlistPort listWishlistPort;
    private final AddWishlistItemPort addWishlistItemPort;
    private final RemoveWishlistItemPort removeWishlistItemPort;

    public WishlistController(
            ListWishlistPort listWishlistPort,
            AddWishlistItemPort addWishlistItemPort,
            RemoveWishlistItemPort removeWishlistItemPort
    ) {
        this.listWishlistPort = listWishlistPort;
        this.addWishlistItemPort = addWishlistItemPort;
        this.removeWishlistItemPort = removeWishlistItemPort;
    }

    @GetMapping
    public List<WishlistItemResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return listWishlistPort.execute(AuthenticatedCustomerId.from(jwt)).stream()
                .map(WishlistItemResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<WishlistItemResponse> add(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddWishlistRequest request
    ) {
        WishlistItem item = addWishlistItemPort.execute(AuthenticatedCustomerId.from(jwt), request.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body(WishlistItemResponse.from(item));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@AuthenticationPrincipal Jwt jwt, @PathVariable String productId) {
        removeWishlistItemPort.remove(AuthenticatedCustomerId.from(jwt), productId);
        return ResponseEntity.noContent().build();
    }
}
