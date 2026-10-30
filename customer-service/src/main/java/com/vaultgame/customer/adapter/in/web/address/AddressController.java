package com.vaultgame.customer.adapter.in.web.address;

import com.vaultgame.customer.adapter.in.web.address.AddressDtos.AddressRequest;
import com.vaultgame.customer.adapter.in.web.address.AddressDtos.AddressResponse;
import com.vaultgame.customer.adapter.in.web.security.AuthenticatedCustomerId;
import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.in.DeleteAddressPort;
import com.vaultgame.customer.application.port.in.ListAddressesPort;
import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.usecase.address.AddressCommand;
import com.vaultgame.customer.domain.model.address.Address;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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

@RestController
@RequestMapping("/customers/me/addresses")
public class AddressController {

    private final ListAddressesPort listAddressesPort;
    private final CreateAddressPort createAddressPort;
    private final UpdateAddressPort updateAddressPort;
    private final DeleteAddressPort deleteAddressPort;

    public AddressController(
            ListAddressesPort listAddressesPort,
            CreateAddressPort createAddressPort,
            UpdateAddressPort updateAddressPort,
            DeleteAddressPort deleteAddressPort
    ) {
        this.listAddressesPort = listAddressesPort;
        this.createAddressPort = createAddressPort;
        this.updateAddressPort = updateAddressPort;
        this.deleteAddressPort = deleteAddressPort;
    }

    @GetMapping
    public List<AddressResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return listAddressesPort.execute(AuthenticatedCustomerId.from(jwt)).stream()
                .map(AddressResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddressRequest request
    ) {
        Address address = createAddressPort.execute(AuthenticatedCustomerId.from(jwt), toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AddressResponse.from(address));
    }

    @PutMapping("/{addressId}")
    public AddressResponse update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        Address address = updateAddressPort.execute(
                AuthenticatedCustomerId.from(jwt),
                addressId,
                toCommand(request)
        );
        return AddressResponse.from(address);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID addressId) {
        deleteAddressPort.execute(AuthenticatedCustomerId.from(jwt), addressId);
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
