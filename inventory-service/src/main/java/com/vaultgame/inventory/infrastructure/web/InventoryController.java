package com.vaultgame.inventory.infrastructure.web;

import com.vaultgame.inventory.application.service.AdjustStockCommand;
import com.vaultgame.inventory.application.service.AdjustStockService;
import com.vaultgame.inventory.application.service.GetAvailabilityService;
import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.infrastructure.web.dto.InventoryDtos;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class InventoryController {

    private final GetAvailabilityService getAvailabilityService;
    private final AdjustStockService adjustStockService;

    public InventoryController(
            GetAvailabilityService getAvailabilityService,
            AdjustStockService adjustStockService
    ) {
        this.getAvailabilityService = getAvailabilityService;
        this.adjustStockService = adjustStockService;
    }

    @GetMapping("/inventory/{productId}")
    public InventoryDtos.InventoryResponse getByProductId(@PathVariable String productId) {
        return InventoryDtos.InventoryResponse.from(getAvailabilityService.getByProductId(productId));
    }

    @GetMapping("/inventory")
    public List<InventoryDtos.InventoryResponse> getByProductIds(@RequestParam String productIds) {
        List<String> ids = Arrays.stream(productIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        return getAvailabilityService.getByProductIds(ids).stream()
                .map(InventoryDtos.InventoryResponse::from)
                .toList();
    }

    @PutMapping("/internal/inventory/{productId}")
    public InventoryDtos.InventoryResponse adjust(
            @PathVariable String productId,
            @Valid @RequestBody InventoryDtos.AdjustInventoryRequest request
    ) {
        InventoryItem item = adjustStockService.adjust(
                new AdjustStockCommand(productId, request.availableQuantity())
        );
        return InventoryDtos.InventoryResponse.from(item);
    }
}
