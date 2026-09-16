package com.vaultgame.api.entity;

import com.vaultgame.api.entity.product.details.ProductDetails;
import com.vaultgame.api.enums.ProductCategory;
import com.vaultgame.api.validation.CategoryMatchesDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@CategoryMatchesDetails
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must have at most 150 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must have at most 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must have at most 100 characters")
    private String brand;

    @Size(max = 5, message = "A product can have at most 5 images")
    private List<String> images;

    @NotNull(message = "Product details are required")
    @Valid
    private ProductDetails details;

    private Map<String, String> extensions;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
