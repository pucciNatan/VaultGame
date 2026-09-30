CREATE TABLE users (
    id             UUID PRIMARY KEY,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    role           VARCHAR(32) NOT NULL,
    enabled        BOOLEAN NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ NOT NULL
);

CREATE TABLE customers (
    id          UUID PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    email       VARCHAR(255) NOT NULL,
    full_name   VARCHAR(255) NOT NULL,
    phone       VARCHAR(32),
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
);

CREATE TABLE addresses (
    id           UUID PRIMARY KEY,
    customer_id  UUID NOT NULL REFERENCES customers (id) ON DELETE CASCADE,
    label        VARCHAR(64) NOT NULL,
    street       VARCHAR(255) NOT NULL,
    number       VARCHAR(32) NOT NULL,
    complement   VARCHAR(255),
    district     VARCHAR(128) NOT NULL,
    city         VARCHAR(128) NOT NULL,
    state        VARCHAR(64) NOT NULL,
    postal_code  VARCHAR(32) NOT NULL,
    country      VARCHAR(2) NOT NULL,
    type         VARCHAR(16) NOT NULL,
    is_default   BOOLEAN NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_addresses_customer_id ON addresses (customer_id);

CREATE TABLE wishlist_items (
    customer_id UUID NOT NULL REFERENCES customers (id) ON DELETE CASCADE,
    product_id  VARCHAR(64) NOT NULL,
    added_at    TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (customer_id, product_id)
);
