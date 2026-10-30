package com.vaultgame.inventory.adapter.in.web.dto;

import com.vaultgame.inventory.domain.model.InventoryItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public final class InventoryDtos {

    private InventoryDtos() {
    }

    public record InventoryResponse(String productId, int availableQuantity) {
        public static InventoryResponse from(InventoryItem item) {
            return new InventoryResponse(item.productId(), item.availableQuantity());
        }
    }

    public record AdjustInventoryRequest(
            @NotNull @Min(0) Integer availableQuantity
    ) {
    }

    public record ReservationLineRequest(
            @NotBlank String productId,
            @NotNull @Min(1) Integer quantity
    ) {
    }

    public record CreateReservationRequest(
            @NotBlank String reference,
            @NotNull List<ReservationLineRequest> lines
    ) {
    }

    public record ReservationLineResponse(String productId, int quantity) {
    }

    public record ReservationResponse(
            String id,
            String reference,
            String status,
            List<ReservationLineResponse> lines,
            String expiresAt
    ) {
    }
}
