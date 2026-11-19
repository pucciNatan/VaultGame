package com.vaultgame.customer.application.usecase.identity;

import com.vaultgame.customer.application.port.in.RegisterPort;
import com.vaultgame.customer.application.event.CustomerRegisteredApplicationEvent;
import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import com.vaultgame.customer.application.port.out.PasswordHasherPort;
import com.vaultgame.customer.application.port.out.TokenIssuerPort;
import com.vaultgame.customer.application.port.out.UserRepositoryPort;
import com.vaultgame.customer.domain.exception.EmailAlreadyRegisteredException;
import com.vaultgame.customer.domain.model.customer.Customer;
import com.vaultgame.customer.domain.model.identity.Role;
import com.vaultgame.customer.domain.model.identity.User;
import org.springframework.context.ApplicationEventPublisher;

public class RegisterUseCase implements RegisterPort {

    private final UserRepositoryPort userRepository;
    private final CustomerRepositoryPort customerRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenIssuerPort tokenIssuer;
    private final ApplicationEventPublisher eventPublisher;

    public RegisterUseCase(
            UserRepositoryPort userRepository,
            CustomerRepositoryPort customerRepository,
            PasswordHasherPort passwordHasher,
            TokenIssuerPort tokenIssuer,
            ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public RegisterResult execute(RegisterCommand command) {
        String email = command.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException(email);
        }

        User user = User.createNew(email, passwordHasher.hash(command.password()), Role.CUSTOMER);
        userRepository.save(user);

        Customer customer = Customer.createForUser(user.id(), email, command.fullName());
        customerRepository.save(customer);

        eventPublisher.publishEvent(new CustomerRegisteredApplicationEvent(customer.id(), email, command.fullName()));

        AuthTokens tokens = tokenIssuer.issueFor(user);
        return new RegisterResult(user, customer, tokens);
    }
}
