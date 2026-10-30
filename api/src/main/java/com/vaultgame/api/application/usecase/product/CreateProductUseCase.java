package com.vaultgame.api.application.usecase.product;

import com.vaultgame.api.application.port.in.CreateProductPort;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.domain.exception.DuplicateProductGtinException;
import com.vaultgame.api.domain.model.Product;

public class CreateProductUseCase implements CreateProductPort {

    private final ProductRepositoryPort productRepository;

    public CreateProductUseCase(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Product product) {
        assertGtinUnique(product);
        return productRepository.save(product);
    }

    private void assertGtinUnique(Product product) {
        if (product.gtin() != null && productRepository.existsByGtin(product.gtin(), product.id())) {
            throw new DuplicateProductGtinException(product.gtin());
        }
    }
}
