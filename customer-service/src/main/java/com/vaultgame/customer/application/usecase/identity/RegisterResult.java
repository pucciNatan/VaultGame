package com.vaultgame.customer.application.usecase.identity;

import com.vaultgame.customer.domain.model.customer.Customer;
import com.vaultgame.customer.domain.model.identity.User;

public record RegisterResult(User user, Customer customer, AuthTokens tokens) {
}
