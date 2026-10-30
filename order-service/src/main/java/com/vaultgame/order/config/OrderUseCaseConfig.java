package com.vaultgame.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.order.application.port.in.CartPort;
import com.vaultgame.order.application.port.in.GetOrderPort;
import com.vaultgame.order.application.port.in.PlaceOrderPort;
import com.vaultgame.order.application.port.out.CartRepositoryPort;
import com.vaultgame.order.application.port.out.CatalogPort;
import com.vaultgame.order.application.port.out.CustomerPort;
import com.vaultgame.order.application.port.out.EmailPort;
import com.vaultgame.order.application.port.out.InventoryPort;
import com.vaultgame.order.application.port.out.OrderRepositoryPort;
import com.vaultgame.order.application.port.out.PaymentPort;
import com.vaultgame.order.application.usecase.CartUseCase;
import com.vaultgame.order.application.usecase.GetOrderUseCase;
import com.vaultgame.order.application.usecase.PlaceOrderUseCase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ServiceUrlsProperties.class)
public class OrderUseCaseConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Bean
    CartPort cartPort(CartRepositoryPort cartRepositoryPort) {
        return new CartUseCase(cartRepositoryPort);
    }

    @Bean
    PlaceOrderPort placeOrderPort(
            CartPort cartPort,
            OrderRepositoryPort orderRepositoryPort,
            CatalogPort catalogPort,
            InventoryPort inventoryPort,
            PaymentPort paymentPort,
            EmailPort emailPort,
            CustomerPort customerPort
    ) {
        return new PlaceOrderUseCase(
                cartPort,
                orderRepositoryPort,
                catalogPort,
                inventoryPort,
                paymentPort,
                emailPort,
                customerPort
        );
    }

    @Bean
    GetOrderPort getOrderPort(OrderRepositoryPort orderRepositoryPort) {
        return new GetOrderUseCase(orderRepositoryPort);
    }
}
