package com.vaultgame.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "vaultgame.checkout.kafka")
public record CheckoutKafkaProperties(Duration replyTimeout) {
    public CheckoutKafkaProperties {
        if (replyTimeout == null) {
            replyTimeout = Duration.ofSeconds(10);
        }
    }
}