package com.vaultgame.fakepaymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FakePaymentGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakePaymentGatewayApplication.class, args);
    }
}
