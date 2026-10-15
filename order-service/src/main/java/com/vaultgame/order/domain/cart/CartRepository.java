package com.vaultgame.order.domain.cart;

import java.util.Optional;

public interface CartRepository {

    Optional<Cart> findByKey(String cartKey);

    void save(Cart cart);

    void delete(String cartKey);
}
