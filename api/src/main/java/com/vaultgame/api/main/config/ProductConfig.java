package com.vaultgame.api.main.config;

import com.vaultgame.api.application.usecases.product.CreateProductUseCase;
import com.vaultgame.api.application.usecases.product.DeleteProductUseCase;
import com.vaultgame.api.application.usecases.product.GetProductByIdUseCase;
import com.vaultgame.api.application.usecases.product.ListProductsUseCase;
import com.vaultgame.api.application.usecases.product.UpdateProductUseCase;
import com.vaultgame.api.domain.gateway.ProductGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    public CreateProductUseCase createProductUseCase(ProductGateway productGateway) {
        return new CreateProductUseCase(productGateway);
    }

    @Bean
    public GetProductByIdUseCase getProductByIdUseCase(ProductGateway productGateway) {
        return new GetProductByIdUseCase(productGateway);
    }

    @Bean
    public ListProductsUseCase listProductsUseCase(ProductGateway productGateway) {
        return new ListProductsUseCase(productGateway);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductGateway productGateway) {
        return new UpdateProductUseCase(productGateway);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductGateway productGateway) {
        return new DeleteProductUseCase(productGateway);
    }
}
