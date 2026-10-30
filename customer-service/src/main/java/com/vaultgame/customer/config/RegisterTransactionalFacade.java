package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.RegisterPort;
import com.vaultgame.customer.application.usecase.identity.RegisterCommand;
import com.vaultgame.customer.application.usecase.identity.RegisterResult;
import com.vaultgame.customer.application.usecase.identity.RegisterUseCase;
import org.springframework.transaction.annotation.Transactional;

public class RegisterTransactionalFacade implements RegisterPort {

    private final RegisterUseCase registerUseCase;

    public RegisterTransactionalFacade(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @Override
    @Transactional
    public RegisterResult execute(RegisterCommand command) {
        return registerUseCase.execute(command);
    }
}
