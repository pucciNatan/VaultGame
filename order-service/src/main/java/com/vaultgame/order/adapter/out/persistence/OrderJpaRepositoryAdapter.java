package com.vaultgame.order.adapter.out.persistence;

import com.vaultgame.order.domain.model.order.Order;
import com.vaultgame.order.application.port.out.OrderRepositoryPort;
import com.vaultgame.order.adapter.out.persistence.entity.OrderEntity;
import com.vaultgame.order.adapter.out.persistence.repository.SpringDataOrderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderJpaRepositoryAdapter implements OrderRepositoryPort {

    private final SpringDataOrderRepository springDataOrderRepository;

    public OrderJpaRepositoryAdapter(SpringDataOrderRepository springDataOrderRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
    }

    @Override
    public Order save(Order order) {
        try {
            OrderEntity entity = springDataOrderRepository.findById(order.id())
                    .map(existing -> updateEntity(existing, order))
                    .orElseGet(() -> PersistenceMapper.toEntity(order));
            return PersistenceMapper.toDomain(springDataOrderRepository.save(entity));
        } catch (DataIntegrityViolationException e) {
            if (order.idempotencyKey() != null) {
                return springDataOrderRepository.findByIdempotencyKey(order.idempotencyKey())
                        .map(PersistenceMapper::toDomain)
                        .orElseThrow(() -> e);
            }
            throw e;
        }
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return springDataOrderRepository.findById(id).map(PersistenceMapper::toDomain);
    }

    @Override
    public Optional<Order> findByIdempotencyKey(String idempotencyKey) {
        return springDataOrderRepository.findByIdempotencyKey(idempotencyKey).map(PersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId, int limit) {
        int pageSize = Math.max(1, Math.min(limit, 100));
        return springDataOrderRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId, PageRequest.of(0, pageSize))
                .stream()
                .map(PersistenceMapper::toDomain)
                .toList();
    }

    private static OrderEntity updateEntity(OrderEntity existing, Order order) {
        existing.setStatus(order.status().name());
        existing.setPaymentTransactionId(order.paymentTransactionId());
        existing.setUpdatedAt(order.updatedAt());
        return existing;
    }
}
