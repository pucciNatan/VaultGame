package com.vaultgame.customer.application.usecase.identity;

import com.vaultgame.customer.application.port.in.GetCurrentUserPort;
import com.vaultgame.customer.application.port.out.UserRepositoryPort;
import com.vaultgame.customer.domain.exception.UserNotFoundException;
import com.vaultgame.customer.domain.model.identity.User;
import java.util.UUID;

public class GetCurrentUserUseCase implements GetCurrentUserPort {

    private final UserRepositoryPort userRepository;

    public GetCurrentUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
