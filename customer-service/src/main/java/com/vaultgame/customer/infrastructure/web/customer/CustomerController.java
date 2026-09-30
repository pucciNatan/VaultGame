package com.vaultgame.customer.infrastructure.web.customer;

import com.vaultgame.customer.application.customer.GetProfileService;
import com.vaultgame.customer.application.customer.UpdateProfileCommand;
import com.vaultgame.customer.application.customer.UpdateProfileService;
import com.vaultgame.customer.domain.customer.Customer;
import com.vaultgame.customer.infrastructure.security.AuthenticatedCustomerId;
import com.vaultgame.customer.infrastructure.web.customer.CustomerDtos.CustomerProfileResponse;
import com.vaultgame.customer.infrastructure.web.customer.CustomerDtos.UpdateProfileRequest;
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

    private final GetProfileService getProfileService;
    private final UpdateProfileService updateProfileService;

    public CustomerController(GetProfileService getProfileService, UpdateProfileService updateProfileService) {
        this.getProfileService = getProfileService;
        this.updateProfileService = updateProfileService;
    }

    @GetMapping
    public CustomerProfileResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        Customer customer = getProfileService.getProfile(AuthenticatedCustomerId.from(jwt));
        return CustomerProfileResponse.from(customer);
    }

    @PatchMapping
    public CustomerProfileResponse updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        Customer customer = updateProfileService.updateProfile(
                AuthenticatedCustomerId.from(jwt),
                new UpdateProfileCommand(request.fullName(), request.phone())
        );
        return CustomerProfileResponse.from(customer);
    }
}
