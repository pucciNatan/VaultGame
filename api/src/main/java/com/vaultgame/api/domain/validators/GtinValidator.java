package com.vaultgame.api.domain.validators;

import com.vaultgame.api.domain.exception.DomainException;

public final class GtinValidator {

    private GtinValidator() {}

    public static String normalize(String raw) {
        if (raw == null) {
            return null;
        }
        String digits = raw.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            throw new DomainException("GTIN must contain digits");
        }
        return digits;
    }

    public static void validate(String normalizedGtin) {
        int length = normalizedGtin.length();
        if (length != 8 && length != 12 && length != 13 && length != 14) {
            throw new DomainException("GTIN must have 8, 12, 13 or 14 digits");
        }
        if (!normalizedGtin.chars().allMatch(Character::isDigit)) {
            throw new DomainException("GTIN must contain only digits");
        }
        int expectedCheckDigit = calculateCheckDigit(normalizedGtin);
        int actualCheckDigit = Character.getNumericValue(normalizedGtin.charAt(length - 1));
        if (expectedCheckDigit != actualCheckDigit) {
            throw new DomainException("Invalid GTIN check digit");
        }
    }

    private static int calculateCheckDigit(String gtin) {
        int length = gtin.length();
        int sum = 0;
        for (int i = 0; i < length - 1; i++) {
            int stepFromRight = (length - 2) - i;
            int weight = (stepFromRight % 2 == 0) ? 3 : 1;
            sum += Character.getNumericValue(gtin.charAt(i)) * weight;
        }
        return (10 - (sum % 10)) % 10;
    }
}
