package com.vaultgame.api.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.vaultgame.api.entity.Product;
import com.vaultgame.api.entity.product.details.BoardGameDetails;
import com.vaultgame.api.entity.product.details.GameDetails;
import com.vaultgame.api.enums.ProductCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CategoryDetailsValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validGameProduct_passesValidation() {
        Product product = validGameProduct();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).isEmpty();
    }

    @Test
    void categoryMismatch_failsValidation() {
        Product product = validGameProduct();
        product.setDetails(new BoardGameDetails(2, 4, 60, 10));

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("details")
                && v.getMessage().contains("GameDetails"));
    }

    @Test
    void tooManyExtensions_failsValidation() {
        Product product = validGameProduct();
        Map<String, String> extensions = new LinkedHashMap<>();
        IntStream.rangeClosed(1, 11).forEach(i -> extensions.put("key" + i, "value"));
        product.setExtensions(extensions);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("extensions")
                && v.getMessage().contains("10"));
    }

    @Test
    void invalidPegiRating_failsFieldValidation() {
        Product product = validGameProduct();
        ((GameDetails) product.getDetails()).setPegiRating(2);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("details.pegiRating"));
    }

    private static Product validGameProduct() {
        Product product = new Product();
        product.setName("Vault Explorer");
        product.setDescription("A game");
        product.setPrice(new BigDecimal("59.99"));
        product.setStock(10);
        product.setCategory(ProductCategory.GAME);
        product.setBrand("Vault Studios");
        product.setDetails(new GameDetails("PS5", 16, 2024));
        product.setExtensions(Map.of("edition", "Deluxe"));
        product.setActive(true);
        return product;
    }
}
