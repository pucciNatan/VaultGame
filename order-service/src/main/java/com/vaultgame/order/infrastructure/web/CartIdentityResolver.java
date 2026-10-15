package com.vaultgame.order.infrastructure.web;

import com.vaultgame.order.domain.cart.CartKeys;
import com.vaultgame.order.domain.cart.MissingCartIdentityException;
import com.vaultgame.order.infrastructure.security.AuthenticatedCustomerId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CartIdentityResolver {

    public String resolveCartKey(String cartIdHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            UUID customerId = AuthenticatedCustomerId.from(jwt);
            return CartKeys.user(customerId);
        }
        if (cartIdHeader == null || cartIdHeader.isBlank()) {
            throw new MissingCartIdentityException();
        }
        return CartKeys.guest(parseCartId(cartIdHeader));
    }

    public String resolveGuestCartKey(String cartIdHeader) {
        if (cartIdHeader == null || cartIdHeader.isBlank()) {
            throw new MissingCartIdentityException();
        }
        return CartKeys.guest(parseCartId(cartIdHeader));
    }

    public UUID requireCustomerId(Jwt jwt) {
        return AuthenticatedCustomerId.from(jwt);
    }

    private static UUID parseCartId(String cartIdHeader) {
        try {
            return UUID.fromString(cartIdHeader.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("X-Cart-Id must be a valid UUID");
        }
    }
}
