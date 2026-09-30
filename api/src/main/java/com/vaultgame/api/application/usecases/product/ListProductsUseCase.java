package com.vaultgame.api.application.usecases.product;

import com.vaultgame.api.domain.entity.Product;
import com.vaultgame.api.domain.gateway.ProductGateway;
import java.util.List;

public class ListProductsUseCase {

    private final ProductGateway productGateway;

    public ListProductsUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public List<Product> execute() {
        return productGateway.findAll();
    }
}
