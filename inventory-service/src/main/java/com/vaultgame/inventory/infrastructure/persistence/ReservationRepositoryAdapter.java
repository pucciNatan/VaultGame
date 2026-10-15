package com.vaultgame.inventory.infrastructure.persistence;

import com.vaultgame.inventory.domain.model.StockReservation;
import com.vaultgame.inventory.domain.repository.ReservationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final SpringDataReservationRepository springDataReservationRepository;

    public ReservationRepositoryAdapter(SpringDataReservationRepository springDataReservationRepository) {
        this.springDataReservationRepository = springDataReservationRepository;
    }

    @Override
    public StockReservation save(StockReservation reservation) {
        return springDataReservationRepository.findById(reservation.id())
                .map(existing -> {
                    existing.setStatus(reservation.status().name());
                    return PersistenceMapper.toDomain(springDataReservationRepository.save(existing));
                })
                .orElseGet(() -> PersistenceMapper.toDomain(
                        springDataReservationRepository.save(PersistenceMapper.toEntity(reservation))
                ));
    }

    @Override
    public Optional<StockReservation> findById(UUID id) {
        return springDataReservationRepository.findById(id).map(PersistenceMapper::toDomain);
    }
}
