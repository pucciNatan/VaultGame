package com.vaultgame.api.infrastructure.web.product;

import com.vaultgame.api.domain.entity.product.details.ProductDetails;
import com.vaultgame.api.domain.enums.ProductCategory;
import java.math.BigDecimal;
import java.util.List;

public record ProductWriteRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductCategory category,
        String brand,
        List<String> images,
        ProductDetails details,
        Boolean active) {
}
