package com.vaultgame.customer.infrastructure.config;

import com.vaultgame.customer.application.identity.RegisterCommand;
import com.vaultgame.customer.application.identity.RegisterResult;
import com.vaultgame.customer.application.identity.RegisterService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RegisterTransactionalFacade {

    private final RegisterService registerService;

    public RegisterTransactionalFacade(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Transactional
    public RegisterResult register(RegisterCommand command) {
        return registerService.register(command);
    }
}
