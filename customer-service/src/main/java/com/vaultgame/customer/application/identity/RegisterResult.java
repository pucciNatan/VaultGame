package com.vaultgame.customer.application.identity;

import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.domain.identity.User;

public record RegisterResult(User user, Customer customer, AuthTokens tokens) {
}
