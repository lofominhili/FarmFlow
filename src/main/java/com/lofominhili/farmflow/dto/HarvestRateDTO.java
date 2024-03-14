package com.lofominhili.farmflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HarvestRateDTO(
        @JsonProperty(value = "product_name")
        @NotBlank(message = "name of product must not be null!")
        String productName,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String measure,
        @NotNull(message = "amount must not be null")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Integer amount,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Integer left
) {
}
