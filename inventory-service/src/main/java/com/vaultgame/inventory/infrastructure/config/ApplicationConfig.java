package com.vaultgame.inventory.infrastructure.config;

import com.vaultgame.inventory.application.service.AdjustStockService;
import com.vaultgame.inventory.application.service.GetAvailabilityService;
import com.vaultgame.inventory.application.service.InventoryReservationFacade;
import com.vaultgame.inventory.application.service.ReservationCoordinator;
import com.vaultgame.inventory.application.service.ReserveStockService;
import com.vaultgame.inventory.domain.repository.InventoryRepository;
import com.vaultgame.inventory.domain.repository.ReservationRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InventoryProperties.class)
public class ApplicationConfig {

    @Bean
    AdjustStockService adjustStockService(InventoryRepository inventoryRepository) {
        return new AdjustStockService(inventoryRepository);
    }

    @Bean
    GetAvailabilityService getAvailabilityService(InventoryRepository inventoryRepository) {
        return new GetAvailabilityService(inventoryRepository);
    }

    @Bean
    InventoryReservationFacade inventoryReservationFacade(
            InventoryRepository inventoryRepository,
            ReservationRepository reservationRepository,
            InventoryProperties inventoryProperties
    ) {
        return new InventoryReservationFacade(
                inventoryRepository,
                reservationRepository,
                inventoryProperties.reservationTtlMinutes()
        );
    }

    @Bean
    ReserveStockService reserveStockService(ReservationCoordinator reservationCoordinator) {
        return new ReserveStockService(reservationCoordinator);
    }
}
