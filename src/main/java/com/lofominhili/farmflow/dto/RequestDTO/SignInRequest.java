package com.lofominhili.farmflow.dto.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @Size(min = 5, max = 35, message = "The email address must contain between 5 and 35 characters")
        @Email(message = "Invalid Email pattern")
        @NotBlank(message = "The 'email' must not be null")
        String email,

        @Size(min = 6, max = 40, message = "The password must contain between 6 and 40 characters")
        @NotNull
        @NotBlank(message = "The 'password' must not be null")
        String password
) {
}

