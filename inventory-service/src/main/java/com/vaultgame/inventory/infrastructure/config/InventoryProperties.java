package com.vaultgame.inventory.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vaultgame.inventory")
public record InventoryProperties(long reservationTtlMinutes) {
}
