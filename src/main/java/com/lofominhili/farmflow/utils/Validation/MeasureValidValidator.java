package com.lofominhili.farmflow.utils.Validation;

import com.lofominhili.farmflow.utils.Measure;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeasureValidValidator implements ConstraintValidator<MeasureValid, String> {

    @Override
    public boolean isValid(String measure, ConstraintValidatorContext constraintValidatorContext) {
        return measure.toUpperCase().equals(Measure.KILOGRAM.toString())
                || measure.toUpperCase().equals(Measure.LITER.toString())
                || measure.toUpperCase().equals(Measure.PIECE.toString());
    }
}
