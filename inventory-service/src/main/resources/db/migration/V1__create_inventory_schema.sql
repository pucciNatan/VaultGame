CREATE TABLE inventory_items (
    product_id          VARCHAR(64) PRIMARY KEY,
    available_quantity  INT NOT NULL CHECK (available_quantity >= 0),
    version             BIGINT NOT NULL DEFAULT 0,
    updated_at          TIMESTAMPTZ NOT NULL
);

CREATE TABLE stock_reservations (
    id          UUID PRIMARY KEY,
    reference   VARCHAR(255) NOT NULL,
    status      VARCHAR(32) NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_stock_reservations_reference ON stock_reservations (reference);

CREATE TABLE stock_reservation_lines (
    reservation_id UUID NOT NULL REFERENCES stock_reservations (id) ON DELETE CASCADE,
    product_id     VARCHAR(64) NOT NULL,
    quantity       INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (reservation_id, product_id)
);
