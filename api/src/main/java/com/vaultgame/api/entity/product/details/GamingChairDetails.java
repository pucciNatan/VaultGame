package com.vaultgame.api.entity.product.details;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("GAMING_CHAIR")
public final class GamingChairDetails implements ProductDetails {

    @NotNull(message = "Maximum weight is required")
    @Min(value = 1, message = "Maximum weight must be at least 1 kg")
    private Integer maxWeightKg;

    @NotBlank(message = "Material is required")
    @Size(max = 100, message = "Material must have at most 100 characters")
    private String material;

    @NotNull(message = "Reclining status is required")
    private Boolean reclining;
}
