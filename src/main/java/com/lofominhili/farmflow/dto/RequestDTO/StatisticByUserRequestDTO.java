package com.lofominhili.farmflow.dto.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StatisticByUserRequestDTO(
        @NotNull(message = "Begin date cannot be null!")
        LocalDate begin,

        @NotNull(message = "End date cannot be null!")
        LocalDate end,

        @NotBlank(message = "Email cannot be null!")
        @Email(message = "Email address must be in the format user@example.com")
        String email
) {
}
