package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.in.DeleteAddressPort;
import com.vaultgame.customer.application.port.in.ListAddressesPort;
import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.application.usecase.address.CreateAddressUseCase;
import com.vaultgame.customer.application.usecase.address.DeleteAddressUseCase;
import com.vaultgame.customer.application.usecase.address.ListAddressesUseCase;
import com.vaultgame.customer.application.usecase.address.UpdateAddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressUseCaseConfig {

    @Bean
    public ListAddressesPort listAddressesPort(AddressRepositoryPort addressRepository) {
        return new ListAddressesUseCase(addressRepository);
    }

    @Bean
    public DeleteAddressPort deleteAddressPort(AddressRepositoryPort addressRepository) {
        return new DeleteAddressUseCase(addressRepository);
    }

    @Bean
    CreateAddressUseCase createAddressUseCase(AddressRepositoryPort addressRepository) {
        return new CreateAddressUseCase(addressRepository);
    }

    @Bean
    UpdateAddressUseCase updateAddressUseCase(AddressRepositoryPort addressRepository) {
        return new UpdateAddressUseCase(addressRepository);
    }

    @Bean
    TransactionalAddressFacade transactionalAddressFacade(
            CreateAddressUseCase createAddressUseCase,
            UpdateAddressUseCase updateAddressUseCase
    ) {
        return new TransactionalAddressFacade(createAddressUseCase, updateAddressUseCase);
    }

    @Bean
    public CreateAddressPort createAddressPort(TransactionalAddressFacade transactionalAddressFacade) {
        return transactionalAddressFacade;
    }

    @Bean
    public UpdateAddressPort updateAddressPort(TransactionalAddressFacade transactionalAddressFacade) {
        return transactionalAddressFacade;
    }
}