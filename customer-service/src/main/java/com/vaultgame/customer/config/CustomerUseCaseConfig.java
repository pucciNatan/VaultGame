package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.GetProfilePort;
import com.vaultgame.customer.application.port.in.UpdateProfilePort;
import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import com.vaultgame.customer.application.usecase.customer.GetProfileUseCase;
import com.vaultgame.customer.application.usecase.customer.UpdateProfileUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerUseCaseConfig {

    @Bean
    GetProfilePort getProfilePort(CustomerRepositoryPort customerRepository) {
        return new GetProfileUseCase(customerRepository);
    }

    @Bean
    UpdateProfilePort updateProfilePort(CustomerRepositoryPort customerRepository) {
        return new UpdateProfileUseCase(customerRepository);
    }
}
