package com.vaultgame.customer.application.identity;

import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.domain.customer.CustomerRepository;
import com.vaultgame.customer.domain.identity.EmailAlreadyRegisteredException;
import com.vaultgame.customer.domain.identity.Role;
import com.vaultgame.customer.domain.identity.User;
import com.vaultgame.customer.domain.identity.UserRepository;

public class RegisterService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public RegisterService(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    public RegisterResult register(RegisterCommand command) {
        String email = command.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException(email);
        }

        User user = User.createNew(email, passwordHasher.hash(command.password()), Role.CUSTOMER);
        userRepository.save(user);

        Customer customer = Customer.createForUser(user.id(), email, command.fullName());
        customerRepository.save(customer);

        AuthTokens tokens = tokenIssuer.issueFor(user);
        return new RegisterResult(user, customer, tokens);
    }
}
