package com.vaultgame.customer.infrastructure.web.customer;

import com.vaultgame.customer.domain.customer.Customer;
import jakarta.validation.constraints.Size;

public final class CustomerDtos {

    private CustomerDtos() {
    }

    public record CustomerProfileResponse(
            String id,
            String email,
            String fullName,
            String phone
    ) {
        public static CustomerProfileResponse from(Customer customer) {
            return new CustomerProfileResponse(
                    customer.id().toString(),
                    customer.email(),
                    customer.fullName(),
                    customer.phone()
            );
        }
    }

    public record UpdateProfileRequest(
            @Size(max = 255) String fullName,
            @Size(max = 32) String phone
    ) {
    }
}
