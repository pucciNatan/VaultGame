package com.vaultgame.order.adapter.out.redis;

import java.util.List;

record RedisCartPayload(List<RedisCartItemPayload> items) {

    record RedisCartItemPayload(String productId, int quantity) {
    }
}
