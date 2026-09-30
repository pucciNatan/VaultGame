package com.vaultgame.api.infrastructure.persistence.entity;

import com.vaultgame.api.domain.entity.product.details.ProductDetails;
import com.vaultgame.api.domain.enums.ProductCategory;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
@NoArgsConstructor
public class ProductDocument {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private ProductCategory category;
    private String brand;

    @Indexed(unique = true, sparse = true)
    private String gtin;

    private List<String> images;
    private ProductDetails details;
    private Boolean active;
}
