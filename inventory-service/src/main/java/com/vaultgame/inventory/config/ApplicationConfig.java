package com.vaultgame.inventory.config;

import com.vaultgame.inventory.application.port.in.AdjustStockPort;
import com.vaultgame.inventory.application.port.in.GetAvailabilityPort;
import com.vaultgame.inventory.application.port.in.ReserveStockPort;
import com.vaultgame.inventory.application.port.out.InventoryRepositoryPort;
import com.vaultgame.inventory.application.port.out.ReservationCoordinatorPort;
import com.vaultgame.inventory.application.port.out.ReservationRepositoryPort;
import com.vaultgame.inventory.application.usecase.AdjustStockUseCase;
import com.vaultgame.inventory.application.usecase.GetAvailabilityUseCase;
import com.vaultgame.inventory.application.usecase.InventoryReservationFacade;
import com.vaultgame.inventory.application.usecase.ReserveStockUseCase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InventoryProperties.class)
public class ApplicationConfig {

    @Bean
    AdjustStockPort adjustStockPort(InventoryRepositoryPort inventoryRepository) {
        return new AdjustStockUseCase(inventoryRepository);
    }

    @Bean
    GetAvailabilityPort getAvailabilityPort(InventoryRepositoryPort inventoryRepository) {
        return new GetAvailabilityUseCase(inventoryRepository);
    }

    @Bean
    InventoryReservationFacade inventoryReservationFacade(
            InventoryRepositoryPort inventoryRepository,
            ReservationRepositoryPort reservationRepository,
            InventoryProperties inventoryProperties
    ) {
        return new InventoryReservationFacade(
                inventoryRepository,
                reservationRepository,
                inventoryProperties.reservationTtlMinutes()
        );
    }

    @Bean
    ReserveStockPort reserveStockPort(ReservationCoordinatorPort reservationCoordinator) {
        return new ReserveStockUseCase(reservationCoordinator);
    }
}
