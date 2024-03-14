package com.lofominhili.farmflow.dto.RequestDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StatisticByFarmRequestDTO(
        @NotNull(message = "begin date must not be null!")
        LocalDate begin,
        @NotNull(message = "end date must not be null!")
        LocalDate end
) {
}
