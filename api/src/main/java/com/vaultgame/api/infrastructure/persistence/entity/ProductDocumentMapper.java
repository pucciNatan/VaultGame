package com.vaultgame.api.infrastructure.persistence.entity;

import com.vaultgame.api.domain.entity.Product;

public final class ProductDocumentMapper {

    private ProductDocumentMapper() {}

    public static ProductDocument toDocument(Product product) {
        ProductDocument document = new ProductDocument();
        document.setId(product.id());
        document.setName(product.name());
        document.setDescription(product.description());
        document.setPrice(product.price());
        document.setStock(product.stock());
        document.setCategory(product.category());
        document.setBrand(product.brand());
        document.setGtin(product.gtin());
        document.setImages(product.images());
        document.setDetails(product.details());
        document.setActive(product.active());
        return document;
    }

    public static Product toDomain(ProductDocument document) {
        return Product.rehydrate(
                document.getId(),
                document.getName(),
                document.getDescription(),
                document.getPrice(),
                document.getStock(),
                document.getCategory(),
                document.getBrand(),
                document.getGtin(),
                document.getImages(),
                document.getDetails(),
                document.getActive());
    }
}
