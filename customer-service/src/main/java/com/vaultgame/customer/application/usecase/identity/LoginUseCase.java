package com.vaultgame.customer.application.usecase.identity;

import com.vaultgame.customer.application.port.in.LoginPort;
import com.vaultgame.customer.application.port.out.PasswordHasherPort;
import com.vaultgame.customer.application.port.out.TokenIssuerPort;
import com.vaultgame.customer.application.port.out.UserRepositoryPort;
import com.vaultgame.customer.domain.exception.InvalidCredentialsException;
import com.vaultgame.customer.domain.model.identity.User;

public class LoginUseCase implements LoginPort {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenIssuerPort tokenIssuer;

    public LoginUseCase(
            UserRepositoryPort userRepository,
            PasswordHasherPort passwordHasher,
            TokenIssuerPort tokenIssuer
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthTokens execute(LoginCommand command) {
        String email = command.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .filter(User::enabled)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return tokenIssuer.issueFor(user);
    }
}
