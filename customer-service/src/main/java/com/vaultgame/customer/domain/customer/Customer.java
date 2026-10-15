package com.vaultgame.customer.domain.customer;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Customer(
        UUID id,
        String email,
        String fullName,
        String phone,
        Instant createdAt,
        Instant updatedAt
) {

    public static Customer createForUser(UUID userId, String email, String fullName) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(fullName, "fullName");
        if (fullName.isBlank()) {
            throw new IllegalArgumentException("fullName must not be blank");
        }
        Instant now = Instant.now();
        return new Customer(userId, email.trim().toLowerCase(), fullName.trim(), null, now, now);
    }

    public static Customer rehydrate(
            UUID id,
            String email,
            String fullName,
            String phone,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Customer(id, email, fullName, phone, createdAt, updatedAt);
    }

    public Customer withProfileUpdate(String fullName, String phone) {
        String updatedName = fullName != null && !fullName.isBlank() ? fullName.trim() : this.fullName;
        String updatedPhone = phone != null ? (phone.isBlank() ? null : phone.trim()) : this.phone;
        return new Customer(id, email, updatedName, updatedPhone, createdAt, Instant.now());
    }
}
