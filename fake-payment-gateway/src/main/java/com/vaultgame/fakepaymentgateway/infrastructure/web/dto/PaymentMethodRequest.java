package com.vaultgame.fakepaymentgateway.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentMethodRequest(
        @NotBlank String type,
        @NotBlank String token
) {
}
