package com.vaultgame.fakepaymentgateway.infrastructure.persistence;

import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import com.vaultgame.fakepaymentgateway.infrastructure.persistence.entity.PaymentEntity;

final class PaymentEntityMapper {

    private PaymentEntityMapper() {
    }

    static PaymentEntity toEntity(Payment payment) {
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

    static Payment toDomain(PaymentEntity entity) {
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
