package com.vaultgame.fakepaymentgateway.application.usecase;

import java.math.BigDecimal;

public record CreatePaymentCommand(
        String reference,
        BigDecimal amount,
        String currency,
        String paymentMethodType,
        String paymentMethodToken,
        String idempotencyKey
) {
}
