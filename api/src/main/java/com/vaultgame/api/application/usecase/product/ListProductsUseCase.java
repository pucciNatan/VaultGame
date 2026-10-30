package com.vaultgame.api.application.usecase.product;

import com.vaultgame.api.application.port.in.ListProductsPort;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.domain.model.Product;
import java.util.List;

public class ListProductsUseCase implements ListProductsPort {

    private final ProductRepositoryPort productRepository;

    public ListProductsUseCase(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> execute() {
        return productRepository.findAll();
    }
}
