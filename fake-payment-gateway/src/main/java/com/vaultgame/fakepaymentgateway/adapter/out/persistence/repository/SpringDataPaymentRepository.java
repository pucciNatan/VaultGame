package com.vaultgame.fakepaymentgateway.adapter.out.persistence.repository;

import com.vaultgame.fakepaymentgateway.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
}
