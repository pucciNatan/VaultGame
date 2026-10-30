package com.vaultgame.customer.application.usecase.identity;

public record AuthTokens(String accessToken, String tokenType, long expiresInSeconds) {
}
