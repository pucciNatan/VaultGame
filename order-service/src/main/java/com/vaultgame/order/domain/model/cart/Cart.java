package com.vaultgame.order.domain.model.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Cart {

    private final String cartKey;
    private final List<CartItem> items;

    public Cart(String cartKey, List<CartItem> items) {
        this.cartKey = Objects.requireNonNull(cartKey, "cartKey");
        this.items = new ArrayList<>(items != null ? items : List.of());
    }

    public static Cart empty(String cartKey) {
        return new Cart(cartKey, List.of());
    }

    public String cartKey() {
        return cartKey;
    }

    public List<CartItem> items() {
        return List.copyOf(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Cart withItems(List<CartItem> newItems) {
        return new Cart(cartKey, newItems);
    }
}
