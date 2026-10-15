package com.vaultgame.customer.domain.address;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Address(
        UUID id,
        UUID customerId,
        String label,
        String street,
        String number,
        String complement,
        String district,
        String city,
        String state,
        String postalCode,
        String country,
        AddressType type,
        boolean isDefault,
        Instant createdAt,
        Instant updatedAt
) {

    public static Address create(
            UUID customerId,
            String label,
            String street,
            String number,
            String complement,
            String district,
            String city,
            String state,
            String postalCode,
            String country,
            AddressType type,
            boolean isDefault
    ) {
        validateRequired(customerId, label, street, number, district, city, state, postalCode, country, type);
        Instant now = Instant.now();
        return new Address(
                UUID.randomUUID(),
                customerId,
                label.trim(),
                street.trim(),
                number.trim(),
                complement != null && !complement.isBlank() ? complement.trim() : null,
                district.trim(),
                city.trim(),
                state.trim(),
                postalCode.trim(),
                country.trim().toUpperCase(),
                type,
                isDefault,
                now,
                now
        );
    }

    public static Address rehydrate(
            UUID id,
            UUID customerId,
            String label,
            String street,
            String number,
            String complement,
            String district,
            String city,
            String state,
            String postalCode,
            String country,
            AddressType type,
            boolean isDefault,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Address(
                id, customerId, label, street, number, complement, district, city, state,
                postalCode, country, type, isDefault, createdAt, updatedAt
        );
    }

    public Address update(
            String label,
            String street,
            String number,
            String complement,
            String district,
            String city,
            String state,
            String postalCode,
            String country,
            AddressType type,
            Boolean isDefault
    ) {
        validateRequired(customerId, label, street, number, district, city, state, postalCode, country, type);
        return new Address(
                id,
                customerId,
                label.trim(),
                street.trim(),
                number.trim(),
                complement != null && !complement.isBlank() ? complement.trim() : null,
                district.trim(),
                city.trim(),
                state.trim(),
                postalCode.trim(),
                country.trim().toUpperCase(),
                type,
                isDefault != null ? isDefault : this.isDefault,
                createdAt,
                Instant.now()
        );
    }

    private static void validateRequired(
            UUID customerId,
            String label,
            String street,
            String number,
            String district,
            String city,
            String state,
            String postalCode,
            String country,
            AddressType type
    ) {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(type, "type");
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label is required");
        }
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("street is required");
        }
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("number is required");
        }
        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("district is required");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("city is required");
        }
        if (state == null || state.isBlank()) {
            throw new IllegalArgumentException("state is required");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("postalCode is required");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("country is required");
        }
    }
}
