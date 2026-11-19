package com.vaultgame.customer.adapter.out.messaging;

import java.util.UUID;

public record CustomerRegisteredPayload(UUID customerId, String email, String fullName) {
}
