package com.vaultgame.fakepaymentgateway.infrastructure.persistence;

import com.vaultgame.fakepaymentgateway.application.service.DuplicateIdempotencyKeyException;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import com.vaultgame.fakepaymentgateway.domain.repository.PaymentRepository;
import com.vaultgame.fakepaymentgateway.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final SpringDataPaymentRepository springDataPaymentRepository;

    public PaymentRepositoryAdapter(SpringDataPaymentRepository springDataPaymentRepository) {
        this.springDataPaymentRepository = springDataPaymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        try {
            PaymentEntity saved = springDataPaymentRepository.save(PaymentEntityMapper.toEntity(payment));
            return PaymentEntityMapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateIdempotencyKeyException(e);
        }
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return springDataPaymentRepository.findById(transactionId).map(PaymentEntityMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return springDataPaymentRepository.findByIdempotencyKey(idempotencyKey).map(PaymentEntityMapper::toDomain);
    }
}
