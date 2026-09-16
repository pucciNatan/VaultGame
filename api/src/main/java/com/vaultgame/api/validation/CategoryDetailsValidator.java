package com.vaultgame.api.validation;

import com.vaultgame.api.entity.Product;
import com.vaultgame.api.entity.product.details.AccessoryDetails;
import com.vaultgame.api.entity.product.details.ActionFigureDetails;
import com.vaultgame.api.entity.product.details.BoardGameDetails;
import com.vaultgame.api.entity.product.details.GameDetails;
import com.vaultgame.api.entity.product.details.GamingChairDetails;
import com.vaultgame.api.entity.product.details.ProductDetails;
import com.vaultgame.api.enums.ProductCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;

public class CategoryDetailsValidator implements ConstraintValidator<CategoryMatchesDetails, Product> {

    private static final int MAX_EXTENSIONS = 10;
    private static final int MAX_EXTENSION_KEY_LENGTH = 50;
    private static final int MAX_EXTENSION_VALUE_LENGTH = 200;

    @Override
    public boolean isValid(Product product, ConstraintValidatorContext context) {
        if (product == null) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        ProductCategory category = product.getCategory();
        ProductDetails details = product.getDetails();

        if (category != null && details != null) {
            Class<?> expected = expectedDetailsClass(category);
            if (!expected.isInstance(details)) {
                context.buildConstraintViolationWithTemplate(
                                "Category " + category + " requires " + expected.getSimpleName())
                        .addPropertyNode("details")
                        .addConstraintViolation();
                valid = false;
            }
        }

        Map<String, String> extensions = product.getExtensions();
        if (extensions != null && !extensions.isEmpty()) {
            if (extensions.size() > MAX_EXTENSIONS) {
                context.buildConstraintViolationWithTemplate(
                                "A product can have at most " + MAX_EXTENSIONS + " extension entries")
                        .addPropertyNode("extensions")
                        .addConstraintViolation();
                valid = false;
            }
            for (Map.Entry<String, String> entry : extensions.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key == null || key.isBlank()) {
                    context.buildConstraintViolationWithTemplate("Extension keys cannot be blank")
                            .addPropertyNode("extensions")
                            .addConstraintViolation();
                    valid = false;
                    continue;
                }
                if (key.length() > MAX_EXTENSION_KEY_LENGTH) {
                    context.buildConstraintViolationWithTemplate(
                                    "Extension key must have at most "
                                            + MAX_EXTENSION_KEY_LENGTH
                                            + " characters")
                            .addPropertyNode("extensions")
                            .addConstraintViolation();
                    valid = false;
                }
                if (value != null && value.length() > MAX_EXTENSION_VALUE_LENGTH) {
                    context.buildConstraintViolationWithTemplate(
                                    "Extension value must have at most "
                                            + MAX_EXTENSION_VALUE_LENGTH
                                            + " characters")
                            .addPropertyNode("extensions")
                            .addConstraintViolation();
                    valid = false;
                }
            }
        }

        return valid;
    }

    private static Class<?> expectedDetailsClass(ProductCategory category) {
        return switch (category) {
            case GAME -> GameDetails.class;
            case BOARD_GAME -> BoardGameDetails.class;
            case ACCESSORY -> AccessoryDetails.class;
            case ACTION_FIGURE -> ActionFigureDetails.class;
            case GAMING_CHAIR -> GamingChairDetails.class;
        };
    }
}
