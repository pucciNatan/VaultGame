package com.vaultgame.order.infrastructure.web;

import com.vaultgame.order.application.cart.CartService;
import com.vaultgame.order.domain.cart.Cart;
import com.vaultgame.order.infrastructure.web.dto.OrderDtos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartIdentityResolver cartIdentityResolver;

    public CartController(CartService cartService, CartIdentityResolver cartIdentityResolver) {
        this.cartService = cartService;
        this.cartIdentityResolver = cartIdentityResolver;
    }

    @GetMapping
    public OrderDtos.CartResponse getCart(@RequestHeader(value = "X-Cart-Id", required = false) String cartId) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        return OrderDtos.CartResponse.from(cartService.getCart(cartKey));
    }

    @PostMapping("/items")
    public ResponseEntity<OrderDtos.CartResponse> addItem(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId,
            @Valid @RequestBody OrderDtos.AddCartItemRequest request
    ) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        Cart cart = cartService.addItem(cartKey, request.productId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderDtos.CartResponse.from(cart));
    }

    @PatchMapping("/items/{productId}")
    public OrderDtos.CartResponse updateItem(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId,
            @PathVariable String productId,
            @Valid @RequestBody OrderDtos.UpdateCartItemRequest request
    ) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        return OrderDtos.CartResponse.from(
                cartService.updateItemQuantity(cartKey, productId, request.quantity())
        );
    }

    @DeleteMapping("/items/{productId}")
    public OrderDtos.CartResponse removeItem(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId,
            @PathVariable String productId
    ) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        return OrderDtos.CartResponse.from(cartService.removeItem(cartKey, productId));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestHeader(value = "X-Cart-Id", required = false) String cartId) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        cartService.clear(cartKey);
    }

    @PostMapping("/merge")
    public OrderDtos.CartResponse merge(
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader("X-Cart-Id") String guestCartId
    ) {
        String userCartKey = cartIdentityResolver.resolveCartKey(null);
        String guestCartKey = cartIdentityResolver.resolveGuestCartKey(guestCartId);
        return OrderDtos.CartResponse.from(cartService.merge(userCartKey, guestCartKey));
    }
}
