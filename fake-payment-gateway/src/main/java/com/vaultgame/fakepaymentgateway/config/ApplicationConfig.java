package com.vaultgame.fakepaymentgateway.config;

import com.vaultgame.fakepaymentgateway.domain.service.PaymentSimulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    PaymentSimulator paymentSimulator(PaymentProperties paymentProperties) {
        return new PaymentSimulator(paymentProperties.timeoutMs());
    }
}
