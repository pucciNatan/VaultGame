package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.validators.DomainAssert.maxLength;
import static com.vaultgame.api.domain.validators.DomainAssert.required;
import static com.vaultgame.api.domain.validators.DomainAssert.requiredText;

import com.vaultgame.api.domain.entity.product.details.specs.HeadsetSpecs;
import com.vaultgame.api.domain.entity.product.details.specs.KeyboardSpecs;
import com.vaultgame.api.domain.entity.product.details.specs.MouseSpecs;
import com.vaultgame.api.domain.entity.product.details.specs.PeripheralSpecs;
import com.vaultgame.api.domain.enums.PeripheralKind;
import com.vaultgame.api.domain.enums.ProductCategory;
import com.vaultgame.api.domain.exception.DomainException;

public record PeripheralDetails(
        PeripheralKind peripheralKind,
        String connectivity,
        String compatibility,
        Boolean rgbLighting,
        PeripheralSpecs specs)
        implements ProductDetails {

    public PeripheralDetails {
        validateFields(peripheralKind, connectivity, compatibility, rgbLighting, specs);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.PERIPHERAL;
    }

    @Override
    public void validate() {
        validateFields(peripheralKind, connectivity, compatibility, rgbLighting, specs);
    }

    public void assertSpecsMatchKind() {
        assertSpecsMatchKind(peripheralKind, specs);
    }

    private static void validateFields(
            PeripheralKind peripheralKind,
            String connectivity,
            String compatibility,
            Boolean rgbLighting,
            PeripheralSpecs specs) {
        required(peripheralKind, "Peripheral kind is required");
        requiredText(connectivity, "Connectivity is required");
        maxLength(connectivity, 100, "Connectivity must have at most 100 characters");
        requiredText(compatibility, "Compatibility is required");
        maxLength(compatibility, 200, "Compatibility must have at most 200 characters");
        required(rgbLighting, "RGB lighting status is required");
        assertSpecsMatchKind(peripheralKind, specs);
        if (specs != null) {
            specs.validate();
        }
    }

    private static void assertSpecsMatchKind(PeripheralKind peripheralKind, PeripheralSpecs specs) {
        Class<?> expected = expectedSpecsClass(peripheralKind);
        if (expected == null) {
            if (specs != null) {
                throw new DomainException("Peripheral kind " + peripheralKind + " must not have specs");
            }
            return;
        }
        if (!expected.isInstance(specs)) {
            throw new DomainException("Peripheral kind " + peripheralKind + " requires " + expected.getSimpleName());
        }
    }

    private static Class<?> expectedSpecsClass(PeripheralKind kind) {
        return switch (kind) {
            case MOUSE -> MouseSpecs.class;
            case KEYBOARD -> KeyboardSpecs.class;
            case HEADSET -> HeadsetSpecs.class;
            case CONTROLLER, OTHER -> null;
        };
    }
}
