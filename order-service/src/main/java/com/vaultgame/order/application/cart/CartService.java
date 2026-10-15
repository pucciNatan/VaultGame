package com.vaultgame.order.application.cart;

import com.vaultgame.order.domain.cart.Cart;
import com.vaultgame.order.domain.cart.CartItem;
import com.vaultgame.order.domain.cart.CartItemNotFoundException;
import com.vaultgame.order.domain.cart.CartRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCart(String cartKey) {
        return cartRepository.findByKey(cartKey).orElse(Cart.empty(cartKey));
    }

    public Cart addItem(String cartKey, String productId, int quantity) {
        Cart cart = getCart(cartKey);
        Map<String, Integer> merged = toQuantityMap(cart.items());
        merged.merge(productId, quantity, Integer::sum);
        Cart updated = cart.withItems(fromQuantityMap(merged));
        cartRepository.save(updated);
        return updated;
    }

    public Cart updateItemQuantity(String cartKey, String productId, int quantity) {
        Cart cart = getCart(cartKey);
        boolean found = cart.items().stream().anyMatch(item -> item.productId().equals(productId));
        if (!found) {
            throw new CartItemNotFoundException(productId);
        }
        Map<String, Integer> merged = toQuantityMap(cart.items());
        merged.put(productId, quantity);
        Cart updated = cart.withItems(fromQuantityMap(merged));
        cartRepository.save(updated);
        return updated;
    }

    public Cart removeItem(String cartKey, String productId) {
        Cart cart = getCart(cartKey);
        Map<String, Integer> merged = toQuantityMap(cart.items());
        if (!merged.containsKey(productId)) {
            throw new CartItemNotFoundException(productId);
        }
        merged.remove(productId);
        Cart updated = cart.withItems(fromQuantityMap(merged));
        cartRepository.save(updated);
        return updated;
    }

    public void clear(String cartKey) {
        cartRepository.delete(cartKey);
    }

    public Cart merge(String userCartKey, String guestCartKey) {
        Cart userCart = getCart(userCartKey);
        Cart guestCart = getCart(guestCartKey);
        Map<String, Integer> merged = toQuantityMap(userCart.items());
        for (CartItem item : guestCart.items()) {
            merged.merge(item.productId(), item.quantity(), Integer::sum);
        }
        Cart updated = userCart.withItems(fromQuantityMap(merged));
        cartRepository.save(updated);
        cartRepository.delete(guestCartKey);
        return updated;
    }

    private static Map<String, Integer> toQuantityMap(List<CartItem> items) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (CartItem item : items) {
            map.put(item.productId(), item.quantity());
        }
        return map;
    }

    private static List<CartItem> fromQuantityMap(Map<String, Integer> map) {
        List<CartItem> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            items.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        return items;
    }
}
