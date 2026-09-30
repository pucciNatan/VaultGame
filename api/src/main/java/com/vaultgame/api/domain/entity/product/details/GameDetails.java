package com.vaultgame.api.domain.entity.product.details;

import static com.vaultgame.api.domain.validators.DomainAssert.maxLength;
import static com.vaultgame.api.domain.validators.DomainAssert.required;
import static com.vaultgame.api.domain.validators.DomainAssert.requiredText;

import com.vaultgame.api.domain.enums.AgeRating;
import com.vaultgame.api.domain.enums.ProductCategory;
import java.time.LocalDate;

public record GameDetails(
        String platform,
        String developer,
        String publisher,
        String genre,
        AgeRating ageRating,
        Boolean physicalMedia,
        LocalDate releaseDate)
        implements ProductDetails {

    public GameDetails {
        validateFields(platform, developer, publisher, genre, ageRating, physicalMedia, releaseDate);
    }

    @Override
    public ProductCategory category() {
        return ProductCategory.GAME;
    }

    @Override
    public void validate() {
        validateFields(platform, developer, publisher, genre, ageRating, physicalMedia, releaseDate);
    }

    private static void validateFields(
            String platform,
            String developer,
            String publisher,
            String genre,
            AgeRating ageRating,
            Boolean physicalMedia,
            LocalDate releaseDate) {
        requiredText(platform, "Platform is required");
        maxLength(platform, 50, "Platform must have at most 50 characters");
        requiredText(developer, "Developer is required");
        maxLength(developer, 150, "Developer must have at most 150 characters");
        requiredText(publisher, "Publisher is required");
        maxLength(publisher, 150, "Publisher must have at most 150 characters");
        requiredText(genre, "Genre is required");
        maxLength(genre, 100, "Genre must have at most 100 characters");
        required(ageRating, "Age rating is required");
        required(physicalMedia, "Physical media status is required");
        required(releaseDate, "Release date is required");
    }
}
