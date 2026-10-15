CREATE TABLE orders (
    id                     UUID PRIMARY KEY,
    customer_id            UUID NOT NULL,
    cart_key               VARCHAR(128) NOT NULL,
    status                 VARCHAR(32) NOT NULL,
    currency               VARCHAR(3) NOT NULL,
    total_amount           NUMERIC(19, 2) NOT NULL,
    payment_transaction_id VARCHAR(64),
    reservation_id         UUID,
    shipping_address_id    UUID NOT NULL,
    idempotency_key        VARCHAR(255),
    request_hash           VARCHAR(64),
    created_at             TIMESTAMPTZ NOT NULL,
    updated_at             TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX idx_orders_idempotency_key
    ON orders (idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX idx_orders_customer_id ON orders (customer_id);

CREATE TABLE order_lines (
    id            UUID PRIMARY KEY,
    order_id      UUID NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id    VARCHAR(64) NOT NULL,
    product_name  VARCHAR(255) NOT NULL,
    unit_price    NUMERIC(19, 2) NOT NULL,
    quantity      INT NOT NULL CHECK (quantity > 0),
    line_total    NUMERIC(19, 2) NOT NULL
);

CREATE INDEX idx_order_lines_order_id ON order_lines (order_id);
