package com.vaultgame.customer.infrastructure.web.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record RegisterRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @NotBlank @Size(max = 255) String fullName
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
    }

    public record RegisterResponse(
            String userId,
            String email,
            String fullName,
            TokenResponse tokens
    ) {
    }

    public record MeResponse(String id, String email, String role) {
    }
}
