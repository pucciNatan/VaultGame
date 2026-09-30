package com.vaultgame.api.application.usecases.product;

import com.vaultgame.api.domain.entity.Product;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import com.vaultgame.api.domain.gateway.ProductGateway;

public class GetProductByIdUseCase {

    private final ProductGateway productGateway;

    public GetProductByIdUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public Product execute(String id) {
        return productGateway.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
