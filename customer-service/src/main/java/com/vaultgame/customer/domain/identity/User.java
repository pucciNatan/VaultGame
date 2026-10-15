package com.vaultgame.customer.domain.identity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record User(
        UUID id,
        String email,
        String passwordHash,
        Role role,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {

    public static User createNew(String email, String passwordHash, Role role) {
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(passwordHash, "passwordHash");
        Objects.requireNonNull(role, "role");
        if (email.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), email.trim().toLowerCase(), passwordHash, role, true, now, now);
    }

    public static User rehydrate(
            UUID id,
            String email,
            String passwordHash,
            Role role,
            boolean enabled,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new User(id, email, passwordHash, role, enabled, createdAt, updatedAt);
    }
}
