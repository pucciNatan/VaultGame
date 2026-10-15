package com.vaultgame.inventory.application.service;

import com.vaultgame.inventory.domain.exception.InsufficientStockException;
import com.vaultgame.inventory.domain.model.InventoryItem;
import com.vaultgame.inventory.domain.model.ReservationLine;
import com.vaultgame.inventory.domain.model.ReservationStatus;
import com.vaultgame.inventory.domain.model.StockReservation;
import com.vaultgame.inventory.domain.repository.InventoryRepository;
import com.vaultgame.inventory.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryReservationFacadeTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private InventoryReservationFacade facade;

    @BeforeEach
    void setUp() {
        facade = new InventoryReservationFacade(inventoryRepository, reservationRepository, 30);
    }

    @Test
    void reserve_decrementsStockAndPersistsReservation() {
        InventoryItem item = InventoryItem.create("p1", 10);
        when(inventoryRepository.findByProductId("p1")).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StockReservation reservation = facade.reserve(
                "order-1",
                List.of(new ReservationLine("p1", 3))
        );

        assertThat(reservation.status()).isEqualTo(ReservationStatus.ACTIVE);
        ArgumentCaptor<InventoryItem> savedItem = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryRepository).save(savedItem.capture());
        assertThat(savedItem.getValue().availableQuantity()).isEqualTo(7);
    }

    @Test
    void reserve_failsWhenInsufficientStock() {
        InventoryItem item = InventoryItem.create("p1", 1);
        when(inventoryRepository.findByProductId("p1")).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> facade.reserve("order-1", List.of(new ReservationLine("p1", 2))))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void release_restoresStock() {
        UUID reservationId = UUID.randomUUID();
        StockReservation active = new StockReservation(
                reservationId,
                "order-1",
                ReservationStatus.ACTIVE,
                List.of(new ReservationLine("p1", 2)),
                Instant.now().plusSeconds(3600),
                Instant.now()
        );
        InventoryItem item = InventoryItem.create("p1", 8);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(active));
        when(inventoryRepository.findByProductId("p1")).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StockReservation released = facade.release(reservationId);

        assertThat(released.status()).isEqualTo(ReservationStatus.RELEASED);
        ArgumentCaptor<InventoryItem> savedItem = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryRepository).save(savedItem.capture());
        assertThat(savedItem.getValue().availableQuantity()).isEqualTo(10);
    }
}
