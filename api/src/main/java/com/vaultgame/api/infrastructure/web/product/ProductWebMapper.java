package com.vaultgame.api.infrastructure.web.product;

import com.vaultgame.api.domain.entity.Product;

public final class ProductWebMapper {

    private ProductWebMapper() {}

    public static Product toNewProduct(ProductWriteRequest request) {
        return Product.create(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.category(),
                request.brand(),
                request.images(),
                request.details(),
                request.active());
    }

    public static Product toUpdatedProduct(String id, ProductWriteRequest request) {
        return Product.restore(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.category(),
                request.brand(),
                request.images(),
                request.details(),
                request.active());
    }
}
