package com.vaultgame.api.entity.product.details;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("ACTION_FIGURE")
public final class ActionFigureDetails implements ProductDetails {

    @NotBlank(message = "Franchise is required")
    @Size(max = 150, message = "Franchise must have at most 150 characters")
    private String franchise;

    @NotNull(message = "Height is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Height must be greater than zero")
    private BigDecimal heightCm;

    @NotNull(message = "Articulated status is required")
    private Boolean articulated;
}
