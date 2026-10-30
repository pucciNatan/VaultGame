package com.vaultgame.fakepaymentgateway.application.port.out;

import com.vaultgame.fakepaymentgateway.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
