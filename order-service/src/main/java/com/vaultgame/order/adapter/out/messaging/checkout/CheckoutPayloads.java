package com.vaultgame.order.adapter.out.messaging.checkout;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class CheckoutPayloads {

    private CheckoutPayloads() {
    }

    public record CatalogResolvePayload(List<String> productIds) {
    }

    public record CatalogProductLine(String id, String name, BigDecimal price, boolean active) {
    }

    public record CatalogResolveReplyData(List<CatalogProductLine> products) {
    }

    public record InventoryReservePayload(String reference, List<InventoryLinePayload> lines) {
    }

    public record InventoryLinePayload(String productId, int quantity) {
    }

    public record InventoryReservationIdPayload(UUID reservationId) {
    }

    public record InventoryReserveReplyData(UUID reservationId) {
    }

    public record PaymentChargePayload(
            String reference,
            BigDecimal amount,
            String currency,
            String paymentMethodType,
            String paymentMethodToken,
            String idempotencyKey
    ) {
    }

    public record PaymentChargeReplyData(String transactionId, String status) {
    }
}