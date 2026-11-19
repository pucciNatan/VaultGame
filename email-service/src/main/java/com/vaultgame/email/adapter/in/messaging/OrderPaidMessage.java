package com.vaultgame.email.adapter.in.messaging;

import java.time.Instant;
import java.util.UUID;

public record OrderPaidMessage(UUID eventId, Instant occurredAt, OrderPaidPayload payload) {
}
