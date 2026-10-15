package com.vaultgame.order.domain.port.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        BigDecimal amount,
        String currency,
        String paymentMethodType,
        String paymentMethodToken
) {
}
