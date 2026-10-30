package com.vaultgame.api.config;

import com.vaultgame.api.application.port.in.CreateProductPort;
import com.vaultgame.api.application.port.in.DeleteProductPort;
import com.vaultgame.api.application.port.in.GetProductByIdPort;
import com.vaultgame.api.application.port.in.ListProductsPort;
import com.vaultgame.api.application.port.in.UpdateProductPort;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.application.usecase.product.CreateProductUseCase;
import com.vaultgame.api.application.usecase.product.DeleteProductUseCase;
import com.vaultgame.api.application.usecase.product.GetProductByIdUseCase;
import com.vaultgame.api.application.usecase.product.ListProductsUseCase;
import com.vaultgame.api.application.usecase.product.UpdateProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductUseCaseConfig {

    @Bean
    public CreateProductPort createProductPort(ProductRepositoryPort productRepository) {
        return new CreateProductUseCase(productRepository);
    }

    @Bean
    public GetProductByIdPort getProductByIdPort(ProductRepositoryPort productRepository) {
        return new GetProductByIdUseCase(productRepository);
    }

    @Bean
    public ListProductsPort listProductsPort(ProductRepositoryPort productRepository) {
        return new ListProductsUseCase(productRepository);
    }

    @Bean
    public UpdateProductPort updateProductPort(ProductRepositoryPort productRepository) {
        return new UpdateProductUseCase(productRepository);
    }

    @Bean
    public DeleteProductPort deleteProductPort(ProductRepositoryPort productRepository) {
        return new DeleteProductUseCase(productRepository);
    }
}
