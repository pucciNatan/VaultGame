package com.vaultgame.fakepaymentgateway.application.simulation;

import com.vaultgame.fakepaymentgateway.domain.exception.InvalidPaymentRequestException;
import com.vaultgame.fakepaymentgateway.domain.exception.PaymentProcessingException;
import com.vaultgame.fakepaymentgateway.domain.exception.PaymentTimeoutException;
import com.vaultgame.fakepaymentgateway.domain.model.PaymentStatus;

import java.util.Set;

public class PaymentSimulator {

    private static final Set<String> KNOWN_TOKENS = Set.of(
            "test-approved",
            "test-declined",
            "test-pending",
            "test-timeout",
            "test-error"
    );

    private final long timeoutMs;

    public PaymentSimulator(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public PaymentStatus simulate(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidPaymentRequestException("INVALID_REQUEST", "paymentMethod.token is required");
        }
        if (!KNOWN_TOKENS.contains(token)) {
            throw new InvalidPaymentRequestException(
                    "INVALID_PAYMENT_TOKEN",
                    "Unknown payment token: " + token
            );
        }

        return switch (token) {
            case "test-approved" -> PaymentStatus.APPROVED;
            case "test-declined" -> PaymentStatus.DECLINED;
            case "test-pending" -> PaymentStatus.PENDING;
            case "test-timeout" -> {
                sleep(timeoutMs);
                throw new PaymentTimeoutException("Payment processing timed out");
            }
            case "test-error" -> throw new PaymentProcessingException("Unable to process payment");
            default -> throw new InvalidPaymentRequestException(
                    "INVALID_PAYMENT_TOKEN",
                    "Unknown payment token: " + token
            );
        };
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PaymentProcessingException("Payment processing interrupted");
        }
    }
}
