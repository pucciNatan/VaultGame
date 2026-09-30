package com.vaultgame.customer.infrastructure.security;

import com.vaultgame.customer.application.identity.AuthTokens;
import com.vaultgame.customer.application.identity.TokenIssuer;
import com.vaultgame.customer.domain.identity.User;
import com.vaultgame.customer.infrastructure.config.JwtProperties;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtTokenIssuer implements TokenIssuer {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public JwtTokenIssuer(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public AuthTokens issueFor(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtProperties.expirationSeconds());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .subject(user.id().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("email", user.email())
                .claim("role", user.role().name())
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new AuthTokens(token, "Bearer", jwtProperties.expirationSeconds());
    }
}
