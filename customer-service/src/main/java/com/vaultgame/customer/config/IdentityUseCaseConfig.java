package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.GetCurrentUserPort;
import com.vaultgame.customer.application.port.in.LoginPort;
import com.vaultgame.customer.application.port.in.RegisterPort;
import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import com.vaultgame.customer.application.port.out.PasswordHasherPort;
import com.vaultgame.customer.application.port.out.TokenIssuerPort;
import com.vaultgame.customer.application.port.out.UserRepositoryPort;
import com.vaultgame.customer.application.usecase.identity.GetCurrentUserUseCase;
import com.vaultgame.customer.application.usecase.identity.LoginUseCase;
import com.vaultgame.customer.application.usecase.identity.RegisterUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityUseCaseConfig {

    @Bean
    RegisterUseCase registerUseCase(
            UserRepositoryPort userRepository,
            CustomerRepositoryPort customerRepository,
            PasswordHasherPort passwordHasher,
            TokenIssuerPort tokenIssuer
    ) {
        return new RegisterUseCase(userRepository, customerRepository, passwordHasher, tokenIssuer);
    }

    @Bean
    RegisterPort registerPort(RegisterUseCase registerUseCase) {
        return new RegisterTransactionalFacade(registerUseCase);
    }

    @Bean
    LoginPort loginPort(
            UserRepositoryPort userRepository,
            PasswordHasherPort passwordHasher,
            TokenIssuerPort tokenIssuer
    ) {
        return new LoginUseCase(userRepository, passwordHasher, tokenIssuer);
    }

    @Bean
    GetCurrentUserPort getCurrentUserPort(UserRepositoryPort userRepository) {
        return new GetCurrentUserUseCase(userRepository);
    }
}
