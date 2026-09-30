package com.vaultgame.fakepaymentgateway.infrastructure.persistence;

import com.vaultgame.fakepaymentgateway.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
}
