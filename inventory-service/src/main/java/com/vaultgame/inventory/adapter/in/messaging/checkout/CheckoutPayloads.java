package com.vaultgame.inventory.adapter.in.messaging.checkout;

import java.util.List;
import java.util.UUID;

public final class CheckoutPayloads {

    private CheckoutPayloads() {
    }

    public record InventoryReservePayload(String reference, List<InventoryLinePayload> lines) {
    }

    public record InventoryLinePayload(String productId, int quantity) {
    }

    public record InventoryReservationIdPayload(UUID reservationId) {
    }

    public record InventoryReserveReplyData(UUID reservationId) {
    }
}