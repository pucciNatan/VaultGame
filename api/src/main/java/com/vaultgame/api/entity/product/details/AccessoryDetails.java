package com.vaultgame.api.entity.product.details;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("ACCESSORY")
public final class AccessoryDetails implements ProductDetails {

    @NotBlank(message = "Accessory type is required")
    @Size(max = 100, message = "Accessory type must have at most 100 characters")
    private String accessoryType;

    @NotBlank(message = "Compatibility is required")
    @Size(max = 200, message = "Compatibility must have at most 200 characters")
    private String compatibility;
}
