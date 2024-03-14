package com.lofominhili.farmflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserDTO(

        @Size(min = 4, max = 15, message = "The name must contain between 4 and 15 characters")
        @NotBlank(message = "name cannot be empty!")
        String name,

        @Size(min = 4, max = 15, message = "The surname address must contain between 4 and 15 characters")
        @NotBlank(message = "surname cannot be empty!")
        String surname,

        @Size(min = 4, max = 15, message = "The patronymic address must contain between 4 and 15 characters")
        @NotBlank(message = "patronymic cannot be empty!")
        String patronymic,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Size(min = 6, max = 40, message = "The password must contain between 6 and 40 characters")
        @NotBlank(message = "Password cannot be empty!")
        String password,

        @Size(min = 5, max = 35, message = "The email address must contain between 5 and 35 characters")
        @NotBlank(message = "Email address cannot be empty")
        @Email(message = "Email address must be in the format user@example.com")
        String email
) {
}
