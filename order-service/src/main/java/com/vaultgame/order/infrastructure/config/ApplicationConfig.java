package com.vaultgame.order.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.order.application.cart.CartService;
import com.vaultgame.order.application.order.GetOrderService;
import com.vaultgame.order.application.order.PlaceOrderService;
import com.vaultgame.order.domain.cart.CartRepository;
import com.vaultgame.order.domain.order.OrderRepository;
import com.vaultgame.order.domain.port.CatalogPort;
import com.vaultgame.order.domain.port.CustomerPort;
import com.vaultgame.order.domain.port.EmailPort;
import com.vaultgame.order.domain.port.InventoryPort;
import com.vaultgame.order.domain.port.PaymentPort;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ServiceUrlsProperties.class)
public class ApplicationConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Bean
    CartService cartService(CartRepository cartRepository) {
        return new CartService(cartRepository);
    }

    @Bean
    PlaceOrderService placeOrderService(
            CartService cartService,
            OrderRepository orderRepository,
            CatalogPort catalogPort,
            InventoryPort inventoryPort,
            PaymentPort paymentPort,
            EmailPort emailPort,
            CustomerPort customerPort
    ) {
        return new PlaceOrderService(
                cartService,
                orderRepository,
                catalogPort,
                inventoryPort,
                paymentPort,
                emailPort,
                customerPort
        );
    }

    @Bean
    GetOrderService getOrderService(OrderRepository orderRepository) {
        return new GetOrderService(orderRepository);
    }
}
