package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.CreateAddressPort;
import com.vaultgame.customer.application.port.in.DeleteAddressPort;
import com.vaultgame.customer.application.port.in.ListAddressesPort;
import com.vaultgame.customer.application.port.in.UpdateAddressPort;
import com.vaultgame.customer.application.port.out.AddressRepositoryPort;
import com.vaultgame.customer.application.usecase.address.AddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressUseCaseConfig {

    @Bean
    AddressUseCase addressUseCase(AddressRepositoryPort addressRepository) {
        return new AddressUseCase(addressRepository);
    }

    @Bean
    TransactionalAddressFacade transactionalAddressFacade(AddressUseCase addressUseCase) {
        return new TransactionalAddressFacade(addressUseCase);
    }

    @Bean
    ListAddressesPort listAddressesPort(AddressUseCase addressUseCase) {
        return addressUseCase;
    }

    @Bean
    DeleteAddressPort deleteAddressPort(AddressUseCase addressUseCase) {
        return addressUseCase;
    }

    @Bean
    CreateAddressPort createAddressPort(TransactionalAddressFacade transactionalAddressFacade) {
        return transactionalAddressFacade;
    }

    @Bean
    UpdateAddressPort updateAddressPort(TransactionalAddressFacade transactionalAddressFacade) {
        return transactionalAddressFacade;
    }
}
