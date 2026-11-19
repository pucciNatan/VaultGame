package com.vaultgame.fakepaymentgateway.adapter.in.messaging.checkout;

import java.math.BigDecimal;

public final class CheckoutPayloads {

    private CheckoutPayloads() {
    }

    public record PaymentChargePayload(
            String reference,
            BigDecimal amount,
            String currency,
            String paymentMethodType,
            String paymentMethodToken,
            String idempotencyKey
    ) {
    }

    public record PaymentChargeReplyData(String transactionId, String status) {
    }
}