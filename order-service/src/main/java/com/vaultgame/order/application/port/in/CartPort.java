package com.vaultgame.order.application.port.in;

import com.vaultgame.order.domain.model.cart.Cart;

public interface CartPort {

    Cart getCart(String cartKey);

    Cart addItem(String cartKey, String productId, int quantity);

    Cart updateItemQuantity(String cartKey, String productId, int quantity);

    Cart removeItem(String cartKey, String productId);

    void clear(String cartKey);

    Cart merge(String userCartKey, String guestCartKey);
}
