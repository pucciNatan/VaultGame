package com.vaultgame.fakepaymentgateway.infrastructure.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank String reference,
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal amount,
        @NotBlank String currency,
        @NotNull @Valid PaymentMethodRequest paymentMethod
) {
}
