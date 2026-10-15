package com.vaultgame.customer.application.identity;

import com.vaultgame.customer.domain.identity.User;
import com.vaultgame.customer.domain.identity.UserNotFoundException;
import com.vaultgame.customer.domain.identity.UserRepository;

import java.util.UUID;

public class GetCurrentUserService {

    private final UserRepository userRepository;

    public GetCurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
