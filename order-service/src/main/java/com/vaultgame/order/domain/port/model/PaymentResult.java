package com.vaultgame.order.domain.port.model;

public record PaymentResult(String transactionId, PaymentStatus status) {

    public enum PaymentStatus {
        APPROVED,
        DECLINED,
        PENDING
    }
}
