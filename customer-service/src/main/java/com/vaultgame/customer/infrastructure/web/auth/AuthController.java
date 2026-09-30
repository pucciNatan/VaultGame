package com.vaultgame.customer.infrastructure.web.auth;

import com.vaultgame.customer.application.identity.AuthTokens;
import com.vaultgame.customer.application.identity.GetCurrentUserService;
import com.vaultgame.customer.application.identity.LoginCommand;
import com.vaultgame.customer.application.identity.LoginService;
import com.vaultgame.customer.application.identity.RegisterCommand;
import com.vaultgame.customer.application.identity.RegisterResult;
import com.vaultgame.customer.domain.identity.User;
import com.vaultgame.customer.infrastructure.config.RegisterTransactionalFacade;
import com.vaultgame.customer.infrastructure.security.AuthenticatedCustomerId;
import com.vaultgame.customer.infrastructure.web.auth.AuthDtos.LoginRequest;
import com.vaultgame.customer.infrastructure.web.auth.AuthDtos.MeResponse;
import com.vaultgame.customer.infrastructure.web.auth.AuthDtos.RegisterRequest;
import com.vaultgame.customer.infrastructure.web.auth.AuthDtos.RegisterResponse;
import com.vaultgame.customer.infrastructure.web.auth.AuthDtos.TokenResponse;
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

    private final RegisterTransactionalFacade registerFacade;
    private final LoginService loginService;
    private final GetCurrentUserService getCurrentUserService;

    public AuthController(
            RegisterTransactionalFacade registerFacade,
            LoginService loginService,
            GetCurrentUserService getCurrentUserService
    ) {
        this.registerFacade = registerFacade;
        this.loginService = loginService;
        this.getCurrentUserService = getCurrentUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResult result = registerFacade.register(
                new RegisterCommand(request.email(), request.password(), request.fullName())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toRegisterResponse(result));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        AuthTokens tokens = loginService.login(new LoginCommand(request.email(), request.password()));
        return toTokenResponse(tokens);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal Jwt jwt) {
        User user = getCurrentUserService.getById(AuthenticatedCustomerId.from(jwt));
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
