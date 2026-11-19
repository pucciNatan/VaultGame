package com.vaultgame.customer.application.event;

import java.util.UUID;

public record CustomerRegisteredApplicationEvent(UUID customerId, String email, String fullName) {
}
