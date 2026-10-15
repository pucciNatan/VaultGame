package com.vaultgame.customer.application.identity;

public record AuthTokens(String accessToken, String tokenType, long expiresInSeconds) {
}
