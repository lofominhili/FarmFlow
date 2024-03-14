package com.lofominhili.farmflow.dto;

import jakarta.validation.constraints.*;

public record RatingDTO(
        @Min(1)
        @Max(5)
        @Positive
        @NotNull(message = "rating cannot be empty!")
        Integer rating,

        @Email(message = "Email address must be in the format user@example.com")
        @NotBlank(message = "Email address cannot be empty!")
        String email
) {
}
