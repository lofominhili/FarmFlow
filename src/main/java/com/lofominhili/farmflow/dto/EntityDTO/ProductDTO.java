package com.lofominhili.farmflow.dto.EntityDTO;

import com.lofominhili.farmflow.utils.Validation.MeasureValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDTO(
        @NotBlank(message = "Name cannot be empty!")
        String name,

        @MeasureValid(message = "Wrong measure!")
        @NotBlank(message = "Measure cannot be empty!")
        String measure,

        @PositiveOrZero(message = "Amount cannot be negative!")
        @NotNull(message = "Amount cannot be empty!")
        Integer amount
) {
}
