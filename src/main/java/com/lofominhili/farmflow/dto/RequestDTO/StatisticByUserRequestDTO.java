package com.lofominhili.farmflow.dto.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StatisticByUserRequestDTO(
        @NotNull(message = "begin date must not be null!")
        LocalDate begin,

        @NotNull(message = "end date must not be null!")
        LocalDate end,

        @Size(min = 5, max = 35, message = "The email address must contain between 5 and 35 characters")
        @NotBlank(message = "email must not be null!")
        @Email(message = "Email address must be in the format user@example.com")
        String email
) {
}
