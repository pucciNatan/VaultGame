package com.vaultgame.order.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultgame.order.domain.cart.Cart;
import com.vaultgame.order.domain.cart.CartItem;
import com.vaultgame.order.domain.cart.CartRepository;
import com.vaultgame.order.infrastructure.config.CartProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class RedisCartRepositoryAdapter implements CartRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Duration cartTtl;

    public RedisCartRepositoryAdapter(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            CartProperties cartProperties
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cartTtl = Duration.ofDays(cartProperties.ttlDays());
    }

    @Override
    public Optional<Cart> findByKey(String cartKey) {
        String json = redisTemplate.opsForValue().get(cartKey);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        try {
            RedisCartPayload payload = objectMapper.readValue(json, RedisCartPayload.class);
            List<CartItem> items = payload.items().stream()
                    .map(item -> new CartItem(item.productId(), item.quantity()))
                    .toList();
            return Optional.of(new Cart(cartKey, items));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid cart payload in Redis for key " + cartKey, e);
        }
    }

    @Override
    public void save(Cart cart) {
        RedisCartPayload payload = new RedisCartPayload(
                cart.items().stream()
                        .map(item -> new RedisCartPayload.RedisCartItemPayload(item.productId(), item.quantity()))
                        .toList()
        );
        try {
            String json = objectMapper.writeValueAsString(payload);
            redisTemplate.opsForValue().set(cart.cartKey(), json, cartTtl);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize cart", e);
        }
    }

    @Override
    public void delete(String cartKey) {
        redisTemplate.delete(cartKey);
    }
}
