package com.vaultgame.api.application.usecases.product;

import com.vaultgame.api.domain.exception.ProductNotFoundException;
import com.vaultgame.api.domain.gateway.ProductGateway;

public class DeleteProductUseCase {

    private final ProductGateway productGateway;

    public DeleteProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public void execute(String id) {
        if (productGateway.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        productGateway.deleteById(id);
    }
}
