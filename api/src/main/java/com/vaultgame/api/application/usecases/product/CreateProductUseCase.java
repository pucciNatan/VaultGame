package com.vaultgame.api.application.usecases.product;

import com.vaultgame.api.domain.entity.Product;
import com.vaultgame.api.domain.exception.DuplicateProductGtinException;
import com.vaultgame.api.domain.gateway.ProductGateway;

public class CreateProductUseCase {

    private final ProductGateway productGateway;

    public CreateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public Product execute(Product product) {
        assertGtinUnique(product);
        return productGateway.save(product);
    }

    private void assertGtinUnique(Product product) {
        if (product.gtin() != null && productGateway.existsByGtin(product.gtin(), product.id())) {
            throw new DuplicateProductGtinException(product.gtin());
        }
    }
}
