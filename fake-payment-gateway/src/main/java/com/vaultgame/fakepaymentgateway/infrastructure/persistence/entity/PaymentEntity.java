package com.vaultgame.fakepaymentgateway.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @Column(name = "transaction_id", nullable = false, length = 64)
    private String transactionId;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus status;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected PaymentEntity() {
    }

    public PaymentEntity(
            String transactionId,
            String reference,
            BigDecimal amount,
            String currency,
            com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus status,
            String idempotencyKey,
            String requestHash,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.transactionId = transactionId;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.requestHash = requestHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getReference() {
        return reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus getStatus() {
        return status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
