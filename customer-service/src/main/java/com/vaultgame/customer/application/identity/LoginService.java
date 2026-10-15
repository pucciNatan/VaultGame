package com.vaultgame.customer.application.identity;

import com.vaultgame.customer.domain.identity.InvalidCredentialsException;
import com.vaultgame.customer.domain.identity.User;
import com.vaultgame.customer.domain.identity.UserRepository;

public class LoginService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public LoginService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    public AuthTokens login(LoginCommand command) {
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
