package com.vaultgame.customer.adapter.out.messaging;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegisteredMessage(UUID eventId, Instant occurredAt, CustomerRegisteredPayload payload) {

    public static CustomerRegisteredMessage create(UUID customerId, String email, String fullName) {
        return new CustomerRegisteredMessage(
                UUID.randomUUID(),
                Instant.now(),
                new CustomerRegisteredPayload(customerId, email, fullName)
        );
    }
}
