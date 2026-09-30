package com.vaultgame.customer.infrastructure.web.address;

import com.vaultgame.customer.application.address.AddressCommand;
import com.vaultgame.customer.application.address.AddressService;
import com.vaultgame.customer.infrastructure.config.TransactionalAddressFacade;
import com.vaultgame.customer.domain.address.Address;
import com.vaultgame.customer.infrastructure.security.AuthenticatedCustomerId;
import com.vaultgame.customer.infrastructure.web.address.AddressDtos.AddressRequest;
import com.vaultgame.customer.infrastructure.web.address.AddressDtos.AddressResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers/me/addresses")
public class AddressController {

    private final AddressService addressService;
    private final TransactionalAddressFacade transactionalAddressFacade;

    public AddressController(AddressService addressService, TransactionalAddressFacade transactionalAddressFacade) {
        this.addressService = addressService;
        this.transactionalAddressFacade = transactionalAddressFacade;
    }

    @GetMapping
    public List<AddressResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return addressService.list(AuthenticatedCustomerId.from(jwt)).stream()
                .map(AddressResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddressRequest request
    ) {
        Address address = transactionalAddressFacade.create(AuthenticatedCustomerId.from(jwt), toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AddressResponse.from(address));
    }

    @PutMapping("/{addressId}")
    public AddressResponse update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        Address address = transactionalAddressFacade.update(AuthenticatedCustomerId.from(jwt), addressId, toCommand(request));
        return AddressResponse.from(address);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID addressId) {
        addressService.delete(AuthenticatedCustomerId.from(jwt), addressId);
        return ResponseEntity.noContent().build();
    }

    private static AddressCommand toCommand(AddressRequest request) {
        return new AddressCommand(
                request.label(),
                request.street(),
                request.number(),
                request.complement(),
                request.district(),
                request.city(),
                request.state(),
                request.postalCode(),
                request.country(),
                request.type(),
                request.isDefault()
        );
    }
}
