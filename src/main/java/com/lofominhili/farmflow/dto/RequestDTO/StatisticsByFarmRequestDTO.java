package com.lofominhili.farmflow.dto.RequestDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StatisticsByFarmRequestDTO(
        @NotNull(message = "Begin date cannot be null!")
        LocalDate begin,

        @NotNull(message = "End date cannot be null!")
        LocalDate end
) {
}
