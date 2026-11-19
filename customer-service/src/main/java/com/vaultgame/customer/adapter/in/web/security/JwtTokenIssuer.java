package com.vaultgame.customer.adapter.in.web.security;

import com.vaultgame.customer.application.port.out.CustomerRepositoryPort;
import com.vaultgame.customer.application.port.out.TokenIssuerPort;
import com.vaultgame.customer.application.usecase.identity.AuthTokens;
import com.vaultgame.customer.config.JwtProperties;
import com.vaultgame.customer.domain.model.identity.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtTokenIssuer implements TokenIssuerPort {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    private final CustomerRepositoryPort customerRepository;

    public JwtTokenIssuer(
            JwtEncoder jwtEncoder,
            JwtProperties jwtProperties,
            CustomerRepositoryPort customerRepository
    ) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
        this.customerRepository = customerRepository;
    }

    @Override
    public AuthTokens issueFor(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtProperties.expirationSeconds());
        String fullName = customerRepository.findById(user.id())
                .map(customer -> customer.fullName())
                .orElse(user.email());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .subject(user.id().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("email", user.email())
                .claim("fullName", fullName)
                .claim("role", user.role().name())
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new AuthTokens(token, "Bearer", jwtProperties.expirationSeconds());
    }
}
