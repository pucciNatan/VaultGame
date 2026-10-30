package com.vaultgame.fakepaymentgateway.application.usecase;

import com.vaultgame.fakepaymentgateway.application.port.in.CreatePaymentPort;
import com.vaultgame.fakepaymentgateway.application.port.out.PaymentRepositoryPort;
import com.vaultgame.fakepaymentgateway.domain.exception.IdempotencyConflictException;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus;
import com.vaultgame.fakepaymentgateway.domain.service.PaymentSimulator;

import java.util.Optional;

public class CreatePaymentUseCase implements CreatePaymentPort {

    private final PaymentRepositoryPort paymentRepository;
    private final PaymentSimulator paymentSimulator;

    public CreatePaymentUseCase(PaymentRepositoryPort paymentRepository, PaymentSimulator paymentSimulator) {
        this.paymentRepository = paymentRepository;
        this.paymentSimulator = paymentSimulator;
    }

    @Override
    public Payment execute(CreatePaymentCommand command) {
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
