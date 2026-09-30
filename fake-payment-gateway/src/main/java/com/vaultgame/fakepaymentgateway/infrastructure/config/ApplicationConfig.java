package com.vaultgame.fakepaymentgateway.infrastructure.config;

import com.vaultgame.fakepaymentgateway.application.service.PaymentService;
import com.vaultgame.fakepaymentgateway.application.simulation.PaymentSimulator;
import com.vaultgame.fakepaymentgateway.domain.repository.PaymentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    PaymentSimulator paymentSimulator(PaymentProperties paymentProperties) {
        return new PaymentSimulator(paymentProperties.timeoutMs());
    }

    @Bean
    PaymentService paymentService(PaymentRepository paymentRepository, PaymentSimulator paymentSimulator) {
        return new PaymentService(paymentRepository, paymentSimulator);
    }
}
