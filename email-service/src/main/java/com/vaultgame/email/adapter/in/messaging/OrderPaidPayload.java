package com.vaultgame.email.adapter.in.messaging;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderPaidPayload(
        UUID orderId,
        UUID customerId,
        String email,
        String fullName,
        BigDecimal total,
        String currency
) {
}
