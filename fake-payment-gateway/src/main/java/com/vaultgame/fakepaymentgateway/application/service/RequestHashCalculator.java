package com.vaultgame.fakepaymentgateway.application.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class RequestHashCalculator {

    private RequestHashCalculator() {
    }

    public static String hash(
            String reference,
            BigDecimal amount,
            String currency,
            String paymentMethodType,
            String paymentMethodToken
    ) {
        String payload = String.join("|",
                reference,
                amount.stripTrailingZeros().toPlainString(),
                currency,
                paymentMethodType,
                paymentMethodToken
        );
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
