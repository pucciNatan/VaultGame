package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.DomainAssert.max;
import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.min;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.ProductCategory;
import com.vaultgame.api.domain.enums.Voltage;

public record HardwareDetails(
        String model,
        String processor,
        String memoryRam,
        String storage,
        Voltage voltage,
        Integer warrantyMonths)
        implements ProductDetails {

    public HardwareDetails {
        validateFields(model, processor, memoryRam, storage, voltage, warrantyMonths);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.HARDWARE;
    }

    @Override
    public void validate() {
        validateFields(model, processor, memoryRam, storage, voltage, warrantyMonths);
    }

    private static void validateFields(
            String model,
            String processor,
            String memoryRam,
            String storage,
            Voltage voltage,
            Integer warrantyMonths) {
        requiredText(model, "Model is required");
        maxLength(model, 100, "Model must have at most 100 characters");
        requiredText(processor, "Processor is required");
        maxLength(processor, 150, "Processor must have at most 150 characters");
        requiredText(memoryRam, "RAM is required");
        maxLength(memoryRam, 50, "RAM must have at most 50 characters");
        requiredText(storage, "Storage is required");
        maxLength(storage, 50, "Storage must have at most 50 characters");
        required(voltage, "Voltage is required");
        required(warrantyMonths, "Warranty is required");
        min(warrantyMonths, 1, "Warranty must be at least 1 month");
        max(warrantyMonths, 120, "Warranty must be at most 120 months");
    }
}
