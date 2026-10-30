package com.vaultgame.fakepaymentgateway.adapter.out.persistence.entity;

import com.vaultgame.fakepaymentgateway.domain.model.Payment;

public final class PaymentEntityMapper {

    private PaymentEntityMapper() {
    }

    public static PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(
                payment.transactionId(),
                payment.reference(),
                payment.amount(),
                payment.currency(),
                payment.status(),
                payment.idempotencyKey(),
                payment.requestHash(),
                payment.createdAt(),
                payment.updatedAt()
        );
    }

    public static Payment toDomain(PaymentEntity entity) {
        return Payment.rehydrate(
                entity.getTransactionId(),
                entity.getReference(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getIdempotencyKey(),
                entity.getRequestHash(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
