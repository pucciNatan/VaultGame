package com.vaultgame.email.adapter.in.messaging;

import java.util.UUID;

public record CustomerRegisteredPayload(UUID customerId, String email, String fullName) {
}
