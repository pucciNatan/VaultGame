package com.vaultgame.api.adapter.in.web.dto;

import com.vaultgame.api.domain.model.product.details.ProductDetails;
import com.vaultgame.api.domain.model.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record ProductWriteRequest(
        @NotBlank(message = "Product name is required") String name,
        @NotBlank(message = "Description is required") String description,
        @NotNull(message = "Price is required") @Positive(message = "Price must be greater than zero") BigDecimal price,
        @NotNull(message = "Category is required") ProductCategory category,
        @NotBlank(message = "Brand is required") String brand,
        String gtin,
        List<String> images,
        @NotNull(message = "Product details are required") ProductDetails details,
        @NotNull(message = "Active status is required") Boolean active) {
}
