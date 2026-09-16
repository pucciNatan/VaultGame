package com.vaultgame.api.entity.product.details;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@JsonTypeName("GAME")
public final class GameDetails implements ProductDetails {

    @NotBlank(message = "Platform is required")
    @Size(max = 50, message = "Platform must have at most 50 characters")
    private String platform;

    @Min(value = 3, message = "PEGI rating must be at least 3")
    @Max(value = 18, message = "PEGI rating must be at most 18")
    private Integer pegiRating;

    @Min(value = 1970, message = "Release year must be at least 1970")
    @Max(value = 2100, message = "Release year must be at most 2100")
    private Integer releaseYear;
}
