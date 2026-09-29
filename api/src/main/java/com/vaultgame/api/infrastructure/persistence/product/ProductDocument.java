package com.vaultgame.api.infrastructure.persistence.product;

import com.vaultgame.api.domain.entity.product.details.ProductDetails;
import com.vaultgame.api.domain.enums.ProductCategory;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Getter
@Setter
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
    private List<String> images;
    private ProductDetails details;
    private Boolean active;
}
