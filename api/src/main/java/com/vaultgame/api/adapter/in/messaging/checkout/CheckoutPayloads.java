package com.vaultgame.api.adapter.in.messaging.checkout;

import java.math.BigDecimal;
import java.util.List;

public final class CheckoutPayloads {

    private CheckoutPayloads() {
    }

    public record CatalogResolvePayload(List<String> productIds) {
    }

    public record CatalogProductLine(String id, String name, BigDecimal price, boolean active) {
    }

    public record CatalogResolveReplyData(List<CatalogProductLine> products) {
    }
}