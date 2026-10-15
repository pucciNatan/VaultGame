package com.vaultgame.customer.infrastructure.web.address;

import com.vaultgame.customer.domain.address.Address;
import com.vaultgame.customer.domain.address.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class AddressDtos {

    private AddressDtos() {
    }

    public record AddressRequest(
            @NotBlank @Size(max = 64) String label,
            @NotBlank @Size(max = 255) String street,
            @NotBlank @Size(max = 32) String number,
            @Size(max = 255) String complement,
            @NotBlank @Size(max = 128) String district,
            @NotBlank @Size(max = 128) String city,
            @NotBlank @Size(max = 64) String state,
            @NotBlank @Size(max = 32) String postalCode,
            @NotBlank @Size(min = 2, max = 2) String country,
            @NotNull AddressType type,
            Boolean isDefault
    ) {
    }

    public record AddressResponse(
            String id,
            String label,
            String street,
            String number,
            String complement,
            String district,
            String city,
            String state,
            String postalCode,
            String country,
            AddressType type,
            boolean isDefault
    ) {
        public static AddressResponse from(Address address) {
            return new AddressResponse(
                    address.id().toString(),
                    address.label(),
                    address.street(),
                    address.number(),
                    address.complement(),
                    address.district(),
                    address.city(),
                    address.state(),
                    address.postalCode(),
                    address.country(),
                    address.type(),
                    address.isDefault()
            );
        }
    }
}
