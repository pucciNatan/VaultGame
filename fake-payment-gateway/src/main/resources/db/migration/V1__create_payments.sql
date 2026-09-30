CREATE TABLE payments (
    transaction_id VARCHAR(64) PRIMARY KEY,
    reference      VARCHAR(255) NOT NULL,
    amount         NUMERIC(19, 2) NOT NULL,
    currency       VARCHAR(3) NOT NULL,
    status         VARCHAR(32) NOT NULL,
    idempotency_key VARCHAR(255),
    request_hash   VARCHAR(64) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX idx_payments_idempotency_key
    ON payments (idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX idx_payments_reference ON payments (reference);
