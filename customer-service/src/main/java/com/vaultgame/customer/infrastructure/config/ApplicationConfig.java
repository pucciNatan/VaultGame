package com.vaultgame.customer.infrastructure.config;

import com.vaultgame.customer.application.address.AddressService;
import com.vaultgame.customer.application.customer.GetProfileService;
import com.vaultgame.customer.application.customer.UpdateProfileService;
import com.vaultgame.customer.application.identity.GetCurrentUserService;
import com.vaultgame.customer.application.identity.LoginService;
import com.vaultgame.customer.application.identity.RegisterService;
import com.vaultgame.customer.application.wishlist.WishlistService;
import com.vaultgame.customer.domain.address.AddressRepository;
import com.vaultgame.customer.domain.customer.CustomerRepository;
import com.vaultgame.customer.domain.identity.UserRepository;
import com.vaultgame.customer.domain.wishlist.WishlistRepository;
import com.vaultgame.customer.application.identity.PasswordHasher;
import com.vaultgame.customer.application.identity.TokenIssuer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    RegisterService registerService(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer
    ) {
        return new RegisterService(userRepository, customerRepository, passwordHasher, tokenIssuer);
    }

    @Bean
    LoginService loginService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer
    ) {
        return new LoginService(userRepository, passwordHasher, tokenIssuer);
    }

    @Bean
    GetCurrentUserService getCurrentUserService(UserRepository userRepository) {
        return new GetCurrentUserService(userRepository);
    }

    @Bean
    GetProfileService getProfileService(CustomerRepository customerRepository) {
        return new GetProfileService(customerRepository);
    }

    @Bean
    UpdateProfileService updateProfileService(CustomerRepository customerRepository) {
        return new UpdateProfileService(customerRepository);
    }

    @Bean
    AddressService addressService(AddressRepository addressRepository) {
        return new AddressService(addressRepository);
    }

    @Bean
    WishlistService wishlistService(WishlistRepository wishlistRepository) {
        return new WishlistService(wishlistRepository);
    }
}
