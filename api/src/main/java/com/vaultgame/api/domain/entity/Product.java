package com.vaultgame.api.domain.entity;

import static com.vaultgame.api.domain.DomainAssert.maxLength;
import static com.vaultgame.api.domain.DomainAssert.min;
import static com.vaultgame.api.domain.DomainAssert.positive;
import static com.vaultgame.api.domain.DomainAssert.required;
import static com.vaultgame.api.domain.DomainAssert.requiredText;

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
        List<String> images,
        ProductDetails details,
        Boolean active) {

    public Product {
        requiredText(name, "Product name is required");
        maxLength(name, 150, "Product name must have at most 150 characters");
        requiredText(description, "Description is required");
        maxLength(description, 2000, "Description must have at most 2000 characters");
        required(price, "Price is required");
        positive(price, "Price must be greater than zero");
        required(stock, "Stock is required");
        min(stock, 0, "Stock cannot be negative");
        required(category, "Category is required");
        requiredText(brand, "Brand is required");
        maxLength(brand, 100, "Brand must have at most 100 characters");
        if (images != null && images.size() > 5) {
            throw new DomainException("A product can have at most 5 images");
        }
        required(details, "Product details are required");
        details.validate();
        if (details.category() != category) {
            throw new DomainException(
                    "Category " + category + " requires " + expectedDetailsName(category));
        }
        required(active, "Active status is required");
    }

    public static Product create(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductCategory category,
            String brand,
            List<String> images,
            ProductDetails details,
            Boolean active) {
        return new Product(null, name, description, price, stock, category, brand, images, details, active);
    }

    public static Product restore(
            String id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            ProductCategory category,
            String brand,
            List<String> images,
            ProductDetails details,
            Boolean active) {
        return new Product(id, name, description, price, stock, category, brand, images, details, active);
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
