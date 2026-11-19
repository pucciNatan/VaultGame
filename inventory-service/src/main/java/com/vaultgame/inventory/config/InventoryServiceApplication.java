package com.vaultgame.inventory.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.vaultgame.inventory")
@EntityScan(basePackages = "com.vaultgame.inventory.adapter.out.persistence.entity")
@EnableJpaRepositories(basePackages = "com.vaultgame.inventory.adapter.out.persistence.repository")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
