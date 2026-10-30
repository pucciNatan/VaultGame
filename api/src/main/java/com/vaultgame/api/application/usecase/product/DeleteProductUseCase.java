package com.vaultgame.api.application.usecase.product;

import com.vaultgame.api.application.port.in.DeleteProductPort;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.domain.exception.ProductNotFoundException;

public class DeleteProductUseCase implements DeleteProductPort {

    private final ProductRepositoryPort productRepository;

    public DeleteProductUseCase(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute(String id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
