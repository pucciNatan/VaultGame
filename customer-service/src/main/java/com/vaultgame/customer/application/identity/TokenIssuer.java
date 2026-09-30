package com.vaultgame.customer.application.identity;

import com.vaultgame.customer.domain.identity.User;

public interface TokenIssuer {

    AuthTokens issueFor(User user);
}
