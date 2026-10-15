package com.vaultgame.customer.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vaultgame.jwt")
public record JwtProperties(String secret, String issuer, long expirationSeconds) {
}
