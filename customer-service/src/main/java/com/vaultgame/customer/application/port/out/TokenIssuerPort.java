package com.vaultgame.customer.application.port.out;

import com.vaultgame.customer.application.usecase.identity.AuthTokens;
import com.vaultgame.customer.domain.model.identity.User;

public interface TokenIssuerPort {

    AuthTokens issueFor(User user);
}
