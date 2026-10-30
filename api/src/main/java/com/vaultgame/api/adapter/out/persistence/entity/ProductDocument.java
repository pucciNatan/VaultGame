package com.vaultgame.api.adapter.out.persistence.entity;

import com.vaultgame.api.domain.model.product.details.ProductDetails;
import com.vaultgame.api.domain.model.enums.ProductCategory;
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
    private ProductCategory category;
    private String brand;

    @Indexed(unique = true, sparse = true)
    private String gtin;

    private List<String> images;
    private ProductDetails details;
    private Boolean active;
}
