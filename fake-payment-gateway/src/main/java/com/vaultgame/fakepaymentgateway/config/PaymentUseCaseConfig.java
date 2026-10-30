package com.vaultgame.fakepaymentgateway.config;

import com.vaultgame.fakepaymentgateway.application.port.in.CreatePaymentPort;
import com.vaultgame.fakepaymentgateway.application.port.in.GetPaymentPort;
import com.vaultgame.fakepaymentgateway.application.port.out.PaymentRepositoryPort;
import com.vaultgame.fakepaymentgateway.application.usecase.CreatePaymentUseCase;
import com.vaultgame.fakepaymentgateway.application.usecase.GetPaymentUseCase;
import com.vaultgame.fakepaymentgateway.domain.service.PaymentSimulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentUseCaseConfig {

    @Bean
    public CreatePaymentPort createPaymentPort(
            PaymentRepositoryPort paymentRepository,
            PaymentSimulator paymentSimulator
    ) {
        return new CreatePaymentUseCase(paymentRepository, paymentSimulator);
    }

    @Bean
    public GetPaymentPort getPaymentPort(PaymentRepositoryPort paymentRepository) {
        return new GetPaymentUseCase(paymentRepository);
    }
}
