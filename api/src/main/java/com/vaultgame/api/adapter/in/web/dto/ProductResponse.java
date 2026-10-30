package com.vaultgame.api.adapter.in.web.dto;

import com.vaultgame.api.domain.model.Product;
import com.vaultgame.api.domain.model.product.details.ProductDetails;
import com.vaultgame.api.domain.model.enums.ProductCategory;
import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        ProductCategory category,
        String brand,
        String gtin,
        List<String> images,
        ProductDetails details,
        Boolean active) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.category(),
                product.brand(),
                product.gtin(),
                product.images(),
                product.details(),
                product.active());
    }
}
