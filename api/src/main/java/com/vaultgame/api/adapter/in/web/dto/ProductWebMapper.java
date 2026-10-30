package com.vaultgame.api.adapter.in.web.dto;

import com.vaultgame.api.domain.model.Product;

public final class ProductWebMapper {

    private ProductWebMapper() {}

    public static Product toNewProduct(ProductWriteRequest request) {
        return Product.create(
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.brand(),
                request.gtin(),
                request.images(),
                request.details(),
                request.active());
    }

    public static Product toUpdatedProduct(String id, ProductWriteRequest request) {
        return Product.forUpdate(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.brand(),
                request.gtin(),
                request.images(),
                request.details(),
                request.active());
    }
}
