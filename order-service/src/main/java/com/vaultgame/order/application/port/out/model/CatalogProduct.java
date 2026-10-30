package com.vaultgame.order.application.port.out.model;

import java.math.BigDecimal;

public record CatalogProduct(String id, String name, BigDecimal price, boolean active) {
}
