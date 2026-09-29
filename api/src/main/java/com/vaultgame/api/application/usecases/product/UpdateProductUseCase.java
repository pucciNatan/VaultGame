package com.vaultgame.api.application.usecases.product;

import com.vaultgame.api.domain.entity.Product;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import com.vaultgame.api.domain.gateway.ProductGateway;

public class UpdateProductUseCase {

    private final ProductGateway productGateway;

    public UpdateProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public Product execute(Product product) {
        String id = product.id();
        if (id == null || productGateway.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        return productGateway.save(product);
    }
}
