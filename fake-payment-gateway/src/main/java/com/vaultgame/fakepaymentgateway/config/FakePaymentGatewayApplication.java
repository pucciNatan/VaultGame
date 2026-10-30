package com.vaultgame.fakepaymentgateway.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.vaultgame.fakepaymentgateway")
@EntityScan(basePackages = "com.vaultgame.fakepaymentgateway.adapter.out.persistence.entity")
@EnableJpaRepositories(basePackages = "com.vaultgame.fakepaymentgateway.adapter.out.persistence")
@ConfigurationPropertiesScan
public class FakePaymentGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakePaymentGatewayApplication.class, args);
    }
}
