package com.lofominhili.farmflow.utils.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation for ensuring that the measure field is valid.
 * This annotation is applied to fields.
 * It specifies the validation logic implemented in {@link MeasureValidValidator}.
 *
 * @author daniel
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MeasureValidValidator.class)
public @interface MeasureValid {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
