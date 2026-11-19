package com.vaultgame.email.adapter.in.messaging;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegisteredMessage(UUID eventId, Instant occurredAt, CustomerRegisteredPayload payload) {
}
