package com.lofominhili.farmflow.dto.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @Email(message = "Email address must be in the format user@example.com")
        @NotBlank(message = "The 'email' cannot be null")
        String email,

        @NotNull
        @NotBlank(message = "The 'password' cannot be null")
        String password
) {
}

