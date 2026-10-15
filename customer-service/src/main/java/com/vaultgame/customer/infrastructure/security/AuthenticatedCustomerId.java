package com.vaultgame.customer.infrastructure.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class AuthenticatedCustomerId {

    private AuthenticatedCustomerId() {
    }

    public static UUID from(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
