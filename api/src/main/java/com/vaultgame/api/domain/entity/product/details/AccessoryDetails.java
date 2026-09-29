package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

import com.vaultgame.api.domain.entity.product.details.specs.AccessorySpecs;
import com.vaultgame.api.domain.entity.product.details.specs.CableSpecs;
import com.vaultgame.api.domain.enums.AccessoryKind;
import com.vaultgame.api.domain.enums.ProductCategory;
import com.vaultgame.api.domain.exception.DomainException;
import java.util.List;

public record AccessoryDetails(
        AccessoryKind accessoryKind,
        List<String> compatibleDevices,
        String material,
        String color,
        AccessorySpecs specs)
        implements ProductDetails {

    public AccessoryDetails {
        validateFields(accessoryKind, compatibleDevices, material, color, specs);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.ACCESSORY;
    }

    @Override
    public void validate() {
        validateFields(accessoryKind, compatibleDevices, material, color, specs);
    }

    public void assertSpecsMatchKind() {
        assertSpecsMatchKind(accessoryKind, specs);
    }

    private static void validateFields(
            AccessoryKind accessoryKind,
            List<String> compatibleDevices,
            String material,
            String color,
            AccessorySpecs specs) {
        required(accessoryKind, "Accessory kind is required");
        required(compatibleDevices, "Compatible devices are required");
        if (compatibleDevices.isEmpty()) {
            throw new DomainException("Compatible devices are required");
        }
        for (String device : compatibleDevices) {
            requiredText(device, "Compatible device cannot be blank");
            maxLength(device, 100, "Compatible device must have at most 100 characters");
        }
        requiredText(material, "Material is required");
        maxLength(material, 100, "Material must have at most 100 characters");
        requiredText(color, "Color is required");
        maxLength(color, 50, "Color must have at most 50 characters");
        assertSpecsMatchKind(accessoryKind, specs);
        if (specs != null) {
            specs.validate();
        }
    }

    private static void assertSpecsMatchKind(AccessoryKind accessoryKind, AccessorySpecs specs) {
        Class<?> expected = expectedSpecsClass(accessoryKind);
        if (expected == null) {
            if (specs != null) {
                throw new DomainException("Accessory kind " + accessoryKind + " must not have specs");
            }
            return;
        }
        if (!expected.isInstance(specs)) {
            throw new DomainException("Accessory kind " + accessoryKind + " requires " + expected.getSimpleName());
        }
    }

    private static Class<?> expectedSpecsClass(AccessoryKind kind) {
        return switch (kind) {
            case CABLE -> CableSpecs.class;
            case CASE, SCREEN_PROTECTOR, GRIP, OTHER -> null;
        };
    }
}
