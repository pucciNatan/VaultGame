package com.vaultgame.order.application.port.out.model;

public record PaymentResult(String transactionId, PaymentStatus status) {

    public enum PaymentStatus {
        APPROVED,
        DECLINED,
        PENDING
    }
}
