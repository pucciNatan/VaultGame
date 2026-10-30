package com.vaultgame.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vaultgame")
public record ServiceUrlsProperties(
        JwtProperties jwt,
        CartProperties cart,
        CatalogProperties catalog,
        InventoryProperties inventory,
        PaymentProperties payment,
        EmailProperties email,
        CustomerProperties customer
) {

    public record CatalogProperties(String baseUrl) {
    }

    public record InventoryProperties(String baseUrl) {
    }

    public record PaymentProperties(String baseUrl) {
    }

    public record EmailProperties(String baseUrl) {
    }

    public record CustomerProperties(String baseUrl) {
    }
}
