package com.vaultgame.customer.adapter.in.web.auth;

import com.vaultgame.customer.adapter.in.web.auth.AuthDtos.LoginRequest;
import com.vaultgame.customer.adapter.in.web.auth.AuthDtos.MeResponse;
import com.vaultgame.customer.adapter.in.web.auth.AuthDtos.RegisterRequest;
import com.vaultgame.customer.adapter.in.web.auth.AuthDtos.RegisterResponse;
import com.vaultgame.customer.adapter.in.web.auth.AuthDtos.TokenResponse;
import com.vaultgame.customer.adapter.in.web.security.AuthenticatedCustomerId;
import com.vaultgame.customer.application.port.in.GetCurrentUserPort;
import com.vaultgame.customer.application.port.in.LoginPort;
import com.vaultgame.customer.application.port.in.RegisterPort;
import com.vaultgame.customer.application.usecase.identity.AuthTokens;
import com.vaultgame.customer.application.usecase.identity.LoginCommand;
import com.vaultgame.customer.application.usecase.identity.RegisterCommand;
import com.vaultgame.customer.application.usecase.identity.RegisterResult;
import com.vaultgame.customer.domain.model.identity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterPort registerPort;
    private final LoginPort loginPort;
    private final GetCurrentUserPort getCurrentUserPort;

    public AuthController(RegisterPort registerPort, LoginPort loginPort, GetCurrentUserPort getCurrentUserPort) {
        this.registerPort = registerPort;
        this.loginPort = loginPort;
        this.getCurrentUserPort = getCurrentUserPort;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResult result = registerPort.execute(
                new RegisterCommand(request.email(), request.password(), request.fullName())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toRegisterResponse(result));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        AuthTokens tokens = loginPort.execute(new LoginCommand(request.email(), request.password()));
        return toTokenResponse(tokens);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal Jwt jwt) {
        User user = getCurrentUserPort.execute(AuthenticatedCustomerId.from(jwt));
        return new MeResponse(user.id().toString(), user.email(), user.role().name());
    }

    private static RegisterResponse toRegisterResponse(RegisterResult result) {
        return new RegisterResponse(
                result.user().id().toString(),
                result.customer().email(),
                result.customer().fullName(),
                toTokenResponse(result.tokens())
        );
    }

    private static TokenResponse toTokenResponse(AuthTokens tokens) {
        return new TokenResponse(tokens.accessToken(), tokens.tokenType(), tokens.expiresInSeconds());
    }
}
