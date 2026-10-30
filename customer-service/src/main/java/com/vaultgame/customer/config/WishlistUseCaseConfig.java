package com.vaultgame.customer.config;

import com.vaultgame.customer.application.port.in.AddWishlistItemPort;
import com.vaultgame.customer.application.port.in.ListWishlistPort;
import com.vaultgame.customer.application.port.in.RemoveWishlistItemPort;
import com.vaultgame.customer.application.port.out.WishlistRepositoryPort;
import com.vaultgame.customer.application.usecase.wishlist.WishlistUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WishlistUseCaseConfig {

    @Bean
    WishlistUseCase wishlistUseCase(WishlistRepositoryPort wishlistRepository) {
        return new WishlistUseCase(wishlistRepository);
    }

    @Bean
    ListWishlistPort listWishlistPort(WishlistUseCase wishlistUseCase) {
        return wishlistUseCase;
    }

    @Bean
    AddWishlistItemPort addWishlistItemPort(WishlistUseCase wishlistUseCase) {
        return wishlistUseCase;
    }

    @Bean
    RemoveWishlistItemPort removeWishlistItemPort(WishlistUseCase wishlistUseCase) {
        return wishlistUseCase;
    }
}
