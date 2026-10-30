package com.vaultgame.order.application.usecase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

public final class CheckoutRequestHashCalculator {

    private CheckoutRequestHashCalculator() {
    }

    public static String hash(
            UUID customerId,
            String cartKey,
            UUID shippingAddressId,
            String paymentMethodType,
            String paymentMethodToken
    ) {
        String payload = customerId + "|" + cartKey + "|" + shippingAddressId + "|"
                + paymentMethodType + "|" + paymentMethodToken;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
