package com.vaultgame.inventory.adapter.in.web;

import com.vaultgame.inventory.adapter.in.web.dto.InventoryDtos;
import com.vaultgame.inventory.application.port.in.AdjustStockPort;
import com.vaultgame.inventory.application.port.in.GetAvailabilityPort;
import com.vaultgame.inventory.domain.model.InventoryItem;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class InventoryController {

    private final GetAvailabilityPort getAvailabilityPort;
    private final AdjustStockPort adjustStockPort;

    public InventoryController(
            GetAvailabilityPort getAvailabilityPort,
            AdjustStockPort adjustStockPort
    ) {
        this.getAvailabilityPort = getAvailabilityPort;
        this.adjustStockPort = adjustStockPort;
    }

    @GetMapping("/inventory/{productId}")
    public InventoryDtos.InventoryResponse getByProductId(@PathVariable String productId) {
        return InventoryDtos.InventoryResponse.from(getAvailabilityPort.getByProductId(productId));
    }

    @GetMapping("/inventory")
    public List<InventoryDtos.InventoryResponse> getByProductIds(@RequestParam String productIds) {
        List<String> ids = Arrays.stream(productIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        return getAvailabilityPort.getByProductIds(ids).stream()
                .map(InventoryDtos.InventoryResponse::from)
                .toList();
    }

    @PutMapping("/internal/inventory/{productId}")
    public InventoryDtos.InventoryResponse adjust(
            @PathVariable String productId,
            @Valid @RequestBody InventoryDtos.AdjustInventoryRequest request
    ) {
        InventoryItem item = adjustStockPort.adjust(productId, request.availableQuantity());
        return InventoryDtos.InventoryResponse.from(item);
    }
}
