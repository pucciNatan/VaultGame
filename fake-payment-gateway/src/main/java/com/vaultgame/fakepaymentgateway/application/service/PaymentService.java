package com.vaultgame.fakepaymentgateway.application.service;

import com.vaultgame.fakepaymentgateway.application.simulation.PaymentSimulator;
import com.vaultgame.fakepaymentgateway.domain.exception.IdempotencyConflictException;
import com.vaultgame.fakepaymentgateway.domain.exception.PaymentNotFoundException;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus;
import com.vaultgame.fakepaymentgateway.domain.repository.PaymentRepository;

import java.util.Optional;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentSimulator paymentSimulator;

    public PaymentService(PaymentRepository paymentRepository, PaymentSimulator paymentSimulator) {
        this.paymentRepository = paymentRepository;
        this.paymentSimulator = paymentSimulator;
    }

    public Payment createPayment(CreatePaymentCommand command) {
        String requestHash = RequestHashCalculator.hash(
                command.reference(),
                command.amount(),
                command.currency(),
                command.paymentMethodType(),
                command.paymentMethodToken()
        );

        String idempotencyKey = normalizeIdempotencyKey(command.idempotencyKey());
        if (idempotencyKey != null) {
            Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return resolveIdempotentReplay(existing.get(), requestHash, idempotencyKey);
            }
        }

        PaymentStatus status = paymentSimulator.simulate(command.paymentMethodToken());

        Payment payment = Payment.create(
                command.reference(),
                command.amount(),
                command.currency(),
                status,
                idempotencyKey,
                requestHash
        );

        try {
            return paymentRepository.save(payment);
        } catch (DuplicateIdempotencyKeyException e) {
            if (idempotencyKey == null) {
                throw e;
            }
            Payment existing = paymentRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> e);
            return resolveIdempotentReplay(existing, requestHash, idempotencyKey);
        }
    }

    public Payment getPayment(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException(transactionId));
    }

    private Payment resolveIdempotentReplay(Payment existing, String requestHash, String idempotencyKey) {
        if (!existing.requestHash().equals(requestHash)) {
            throw new IdempotencyConflictException(idempotencyKey);
        }
        return existing;
    }

    private static String normalizeIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return null;
        }
        return idempotencyKey.trim();
    }
}
