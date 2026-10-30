package com.vaultgame.api.application.usecase.product;

import com.vaultgame.api.application.port.in.GetProductByIdPort;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import com.vaultgame.api.domain.model.Product;

public class GetProductByIdUseCase implements GetProductByIdPort {

    private final ProductRepositoryPort productRepository;

    public GetProductByIdUseCase(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
