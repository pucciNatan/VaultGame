package com.vaultgame.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CategoryDetailsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryMatchesDetails {

    String message() default "Product category does not match details type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
