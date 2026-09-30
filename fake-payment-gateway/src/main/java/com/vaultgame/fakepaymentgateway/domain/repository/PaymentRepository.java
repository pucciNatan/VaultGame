package com.vaultgame.fakepaymentgateway.domain.repository;

import com.vaultgame.fakepaymentgateway.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
