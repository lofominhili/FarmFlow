package com.lofominhili.farmflow.utils.Validation;

import com.lofominhili.farmflow.utils.Measure;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Validator for ensuring that the measure field is valid.
 * This validator implements the {@link ConstraintValidator} interface.
 * It is associated with the {@link MeasureValid} annotation.
 *
 * @author daniel
 */
@Component
public class MeasureValidValidator implements ConstraintValidator<MeasureValid, String> {
    /**
     * Checks whether the provided measure value is valid.
     * The measure value is considered valid if it matches one of the predefined measure types.
     *
     * @param measure                    The measure value to validate.
     * @param constraintValidatorContext The context in which the constraint is evaluated.
     * @return true if the measure value is valid, false otherwise.
     */
    @Override
    public boolean isValid(String measure, ConstraintValidatorContext constraintValidatorContext) {
        return measure.toUpperCase().equals(Measure.KILOGRAM.toString())
                || measure.toUpperCase().equals(Measure.LITER.toString())
                || measure.toUpperCase().equals(Measure.PIECE.toString());
    }
}
