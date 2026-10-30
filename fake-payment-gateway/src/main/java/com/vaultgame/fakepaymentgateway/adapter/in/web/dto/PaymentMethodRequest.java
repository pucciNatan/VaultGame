package com.vaultgame.fakepaymentgateway.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentMethodRequest(
        @NotBlank String type,
        @NotBlank String token
) {
}
