package com.vaultgame.customer.application.port.in;

import com.vaultgame.customer.application.usecase.identity.AuthTokens;
import com.vaultgame.customer.application.usecase.identity.LoginCommand;

public interface LoginPort {
    AuthTokens execute(LoginCommand command);
}
