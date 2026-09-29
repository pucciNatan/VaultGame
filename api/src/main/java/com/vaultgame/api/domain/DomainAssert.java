package com.vaultgame.api.domain;

import com.vaultgame.api.domain.exception.DomainException;
import java.math.BigDecimal;

public final class DomainAssert {

    private DomainAssert() {}

    public static void required(Object value, String message) {
        if (value == null) {
            throw new DomainException(message);
        }
    }

    public static void requiredText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
    }

    public static void maxLength(String value, int max, String message) {
        if (value != null && value.length() > max) {
            throw new DomainException(message);
        }
    }

    public static void min(Integer value, int min, String message) {
        if (value != null && value < min) {
            throw new DomainException(message);
        }
    }

    public static void max(Integer value, int max, String message) {
        if (value != null && value > max) {
            throw new DomainException(message);
        }
    }

    public static void positive(BigDecimal value, String message) {
        if (value != null && value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(message);
        }
    }

    public static void positive(BigDecimal value, String message, boolean allowNull) {
        if (value == null) {
            if (!allowNull) {
                throw new DomainException(message);
            }
            return;
        }
        positive(value, message);
    }
}
