package com.vaultgame.inventory.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record StockReservation(
        UUID id,
        String reference,
        ReservationStatus status,
        List<ReservationLine> lines,
        Instant expiresAt,
        Instant createdAt
) {

    public StockReservation {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(lines, "lines");
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be empty");
        }
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(createdAt, "createdAt");
        if (reference.isBlank()) {
            throw new IllegalArgumentException("reference must not be blank");
        }
    }

    public static StockReservation createActive(
            String reference,
            List<ReservationLine> lines,
            Instant expiresAt
    ) {
        return new StockReservation(
                UUID.randomUUID(),
                reference,
                ReservationStatus.ACTIVE,
                List.copyOf(lines),
                expiresAt,
                Instant.now()
        );
    }

    public StockReservation withStatus(ReservationStatus newStatus) {
        return new StockReservation(id, reference, newStatus, lines, expiresAt, createdAt);
    }
}
