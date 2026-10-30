package com.vaultgame.order.application.port.out;

import com.vaultgame.order.domain.model.cart.Cart;

import java.util.Optional;

public interface CartRepositoryPort {

    Optional<Cart> findByKey(String cartKey);

    void save(Cart cart);

    void delete(String cartKey);
}
