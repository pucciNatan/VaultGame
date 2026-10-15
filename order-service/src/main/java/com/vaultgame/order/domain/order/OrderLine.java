package com.vaultgame.order.domain.order;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record OrderLine(
        UUID id,
        String productId,
        String productName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) {

    public OrderLine {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(productName, "productName");
        Objects.requireNonNull(unitPrice, "unitPrice");
        Objects.requireNonNull(lineTotal, "lineTotal");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }

    public static OrderLine create(
            String productId,
            String productName,
            BigDecimal unitPrice,
            int quantity
    ) {
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new OrderLine(UUID.randomUUID(), productId, productName, unitPrice, quantity, lineTotal);
    }
}
