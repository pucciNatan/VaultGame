package com.vaultgame.api.domain.entity;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.min;
import static com.vaultgame.api.domain.DomainAssert.positive;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

import com.vaultgame.api.domain.GtinValidator;
import com.vaultgame.api.domain.entity.product.details.ProductDetails;
import com.vaultgame.api.domain.enums.ProductCategory;
import com.vaultgame.api.domain.exception.DomainException;
import java.math.BigDecimal;
import java.util.List;

public record Product(
        String id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductCategory category,
        String brand,
        String gtin,
        List<String> images,
        ProductDetails details,
        Boolean active) {

    public Product {
        if (gtin != null && gtin.isBlank()) {
            gtin = null;
        }
    }

    public static Product create(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductCategory category,
            String brand,
            String gtin,
            List<String> images,
            ProductDetails details,
            Boolean active) {
        String normalizedGtin = validateAndNormalizeGtinForWrite(category, gtin);
        Product product = new Product(
                null, name, description, price, stock, category, brand, normalizedGtin, images, details, active);
        validateForWrite(product);
        return product;
    }

    public static Product forUpdate(
            String id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductCategory category,
            String brand,
            String gtin,
            List<String> images,
            ProductDetails details,
            Boolean active) {
        String normalizedGtin = validateAndNormalizeGtinForWrite(category, gtin);
        Product product = new Product(
                id, name, description, price, stock, category, brand, normalizedGtin, images, details, active);
        validateForWrite(product);
        return product;
    }

    public static Product rehydrate(
            String id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductCategory category,
            String brand,
            String gtin,
            List<String> images,
            ProductDetails details,
            Boolean active) {
        return new Product(
                id, name, description, price, stock, category, brand, gtin, images, details, active);
    }

    private static void validateForWrite(Product product) {
        requiredText(product.name(), "Product name is required");
        maxLength(product.name(), 150, "Product name must have at most 150 characters");
        requiredText(product.description(), "Description is required");
        maxLength(product.description(), 2000, "Description must have at most 2000 characters");
        required(product.price(), "Price is required");
        positive(product.price(), "Price must be greater than zero");
        required(product.stock(), "Stock is required");
        min(product.stock(), 0, "Stock cannot be negative");
        required(product.category(), "Category is required");
        requiredText(product.brand(), "Brand is required");
        maxLength(product.brand(), 100, "Brand must have at most 100 characters");
        if (product.images() != null && product.images().size() > 5) {
            throw new DomainException("A product can have at most 5 images");
        }
        required(product.details(), "Product details are required");
        product.details().validate();
        if (product.details().category() != product.category()) {
            throw new DomainException(
                    "Category " + product.category() + " requires " + expectedDetailsName(product.category()));
        }
        required(product.active(), "Active status is required");
    }

    private static String validateAndNormalizeGtinForWrite(ProductCategory category, String rawGtin) {
        if (category == ProductCategory.DIGITAL_CARD) {
            if (rawGtin != null && !rawGtin.isBlank()) {
                throw new DomainException("Digital card products must not have a GTIN");
            }
            return null;
        }
        requiredText(rawGtin, "GTIN is required");
        String normalized = GtinValidator.normalize(rawGtin);
        GtinValidator.validate(normalized);
        return normalized;
    }

    private static String expectedDetailsName(ProductCategory category) {
        return switch (category) {
            case GAME -> "GameDetails";
            case BOARD_GAME -> "BoardGameDetails";
            case HARDWARE -> "HardwareDetails";
            case PERIPHERAL -> "PeripheralDetails";
            case ACCESSORY -> "AccessoryDetails";
            case FURNITURE -> "FurnitureDetails";
            case ACTION_FIGURE -> "ActionFigureDetails";
            case MERCHANDISE -> "MerchandiseDetails";
            case DIGITAL_CARD -> "DigitalCardDetails";
        };
    }
}
