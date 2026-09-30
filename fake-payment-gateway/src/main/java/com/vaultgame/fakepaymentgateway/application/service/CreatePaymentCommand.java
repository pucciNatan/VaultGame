package com.vaultgame.fakepaymentgateway.application.service;

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
