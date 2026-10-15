package com.vaultgame.order.infrastructure.redis;

import java.util.List;

record RedisCartPayload(List<RedisCartItemPayload> items) {

    record RedisCartItemPayload(String productId, int quantity) {
    }
}
