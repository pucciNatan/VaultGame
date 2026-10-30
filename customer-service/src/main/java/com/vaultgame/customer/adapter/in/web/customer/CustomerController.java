package com.vaultgame.customer.adapter.in.web.customer;

import com.vaultgame.customer.adapter.in.web.customer.CustomerDtos.CustomerProfileResponse;
import com.vaultgame.customer.adapter.in.web.customer.CustomerDtos.UpdateProfileRequest;
import com.vaultgame.customer.adapter.in.web.security.AuthenticatedCustomerId;
import com.vaultgame.customer.application.port.in.GetProfilePort;
import com.vaultgame.customer.application.port.in.UpdateProfilePort;
import com.vaultgame.customer.application.usecase.customer.UpdateProfileCommand;
import com.vaultgame.customer.domain.model.customer.Customer;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers/me")
public class CustomerController {

    private final GetProfilePort getProfilePort;
    private final UpdateProfilePort updateProfilePort;

    public CustomerController(GetProfilePort getProfilePort, UpdateProfilePort updateProfilePort) {
        this.getProfilePort = getProfilePort;
        this.updateProfilePort = updateProfilePort;
    }

    @GetMapping
    public CustomerProfileResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        Customer customer = getProfilePort.execute(AuthenticatedCustomerId.from(jwt));
        return CustomerProfileResponse.from(customer);
    }

    @PatchMapping
    public CustomerProfileResponse updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        Customer customer = updateProfilePort.execute(
                AuthenticatedCustomerId.from(jwt),
                new UpdateProfileCommand(request.fullName(), request.phone())
        );
        return CustomerProfileResponse.from(customer);
    }
}
