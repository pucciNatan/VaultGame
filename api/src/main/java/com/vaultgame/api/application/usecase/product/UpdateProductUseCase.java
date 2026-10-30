package com.vaultgame.api.application.usecase.product;

import com.vaultgame.api.application.port.in.UpdateProductPort;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.domain.exception.DuplicateProductGtinException;
import com.vaultgame.api.domain.exception.ProductNotFoundException;
import com.vaultgame.api.domain.model.Product;

public class UpdateProductUseCase implements UpdateProductPort {

    private final ProductRepositoryPort productRepository;

    public UpdateProductUseCase(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Product product) {
        String id = product.id();
        if (id == null || productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        assertGtinUnique(product);
        return productRepository.save(product);
    }

    private void assertGtinUnique(Product product) {
        if (product.gtin() != null && productRepository.existsByGtin(product.gtin(), product.id())) {
            throw new DuplicateProductGtinException(product.gtin());
        }
    }
}
