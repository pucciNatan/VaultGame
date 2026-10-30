package com.vaultgame.fakepaymentgateway.adapter.out.persistence;

import com.vaultgame.fakepaymentgateway.adapter.out.persistence.entity.PaymentEntityMapper;
import com.vaultgame.fakepaymentgateway.adapter.out.persistence.repository.SpringDataPaymentRepository;
import com.vaultgame.fakepaymentgateway.application.port.out.PaymentRepositoryPort;
import com.vaultgame.fakepaymentgateway.application.usecase.DuplicateIdempotencyKeyException;
import com.vaultgame.fakepaymentgateway.domain.model.Payment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final SpringDataPaymentRepository springDataPaymentRepository;

    public JpaPaymentRepositoryAdapter(SpringDataPaymentRepository springDataPaymentRepository) {
        this.springDataPaymentRepository = springDataPaymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        try {
            var saved = springDataPaymentRepository.save(PaymentEntityMapper.toEntity(payment));
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
