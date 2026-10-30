package com.vaultgame.order.adapter.in.web.controllers;

import com.vaultgame.order.adapter.in.web.CartIdentityResolver;
import com.vaultgame.order.adapter.in.web.dto.OrderDtos;
import com.vaultgame.order.application.port.in.CartPort;
import com.vaultgame.order.domain.model.cart.Cart;
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

    private final CartPort cartPort;
    private final CartIdentityResolver cartIdentityResolver;

    public CartController(CartPort cartPort, CartIdentityResolver cartIdentityResolver) {
        this.cartPort = cartPort;
        this.cartIdentityResolver = cartIdentityResolver;
    }

    @GetMapping
    public OrderDtos.CartResponse getCart(@RequestHeader(value = "X-Cart-Id", required = false) String cartId) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        return OrderDtos.CartResponse.from(cartPort.getCart(cartKey));
    }

    @PostMapping("/items")
    public ResponseEntity<OrderDtos.CartResponse> addItem(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId,
            @Valid @RequestBody OrderDtos.AddCartItemRequest request
    ) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        Cart cart = cartPort.addItem(cartKey, request.productId(), request.quantity());
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
                cartPort.updateItemQuantity(cartKey, productId, request.quantity())
        );
    }

    @DeleteMapping("/items/{productId}")
    public OrderDtos.CartResponse removeItem(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId,
            @PathVariable String productId
    ) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        return OrderDtos.CartResponse.from(cartPort.removeItem(cartKey, productId));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestHeader(value = "X-Cart-Id", required = false) String cartId) {
        String cartKey = cartIdentityResolver.resolveCartKey(cartId);
        cartPort.clear(cartKey);
    }

    @PostMapping("/merge")
    public OrderDtos.CartResponse merge(
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader("X-Cart-Id") String guestCartId
    ) {
        String userCartKey = cartIdentityResolver.resolveCartKey(null);
        String guestCartKey = cartIdentityResolver.resolveGuestCartKey(guestCartId);
        return OrderDtos.CartResponse.from(cartPort.merge(userCartKey, guestCartKey));
    }
}
