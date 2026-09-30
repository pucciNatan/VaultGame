package com.vaultgame.fakepaymentgateway.infrastructure.web.dto;

import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus;

public record PaymentResponse(
        String transactionId,
        String reference,
        PaymentStatus status
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.transactionId(),
                payment.reference(),
                payment.status()
        );
    }
}
