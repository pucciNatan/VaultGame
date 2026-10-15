package com.vaultgame.order.domain.port.model;

import java.math.BigDecimal;

public record CatalogProduct(String id, String name, BigDecimal price, boolean active) {
}
