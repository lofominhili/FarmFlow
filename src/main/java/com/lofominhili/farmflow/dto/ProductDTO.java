package com.lofominhili.farmflow.dto;

import com.lofominhili.farmflow.utils.Validation.MeasureValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductDTO(
        @Size(min = 4, max = 15)
        @NotBlank(message = "name cannot be empty!")
        String name,

        @MeasureValid(message = "wrong measure!")
        @NotBlank(message = "measure cannot be empty!")
        String measure,

        @PositiveOrZero(message = "amount cannot be negative!")
        @NotNull(message = "amount cannot be empty!")
        Integer amount
) {
}
