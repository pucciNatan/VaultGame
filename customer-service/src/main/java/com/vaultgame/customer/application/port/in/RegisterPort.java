package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.application.usecase.identity.RegisterCommand;
import com.vaultgame.customer.application.usecase.identity.RegisterResult;

public interface RegisterPort {
    RegisterResult execute(RegisterCommand command);
}
