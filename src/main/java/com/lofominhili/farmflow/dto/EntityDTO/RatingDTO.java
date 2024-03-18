package com.lofominhili.farmflow.dto.EntityDTO;

import jakarta.validation.constraints.*;

public record RatingDTO(
        @Min(value = 1, message = "The minimum rating value cannot be lower than 1")
        @Max(value = 5, message = "The maximum rating value cannot be higher than 1")
        @Positive
        @NotNull(message = "Rating cannot be empty!")
        Integer rating,

        @Email(message = "Email address must be in the format user@example.com")
        @NotBlank(message = "Email address cannot be empty!")
        String email
) {
}
