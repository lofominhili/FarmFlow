package com.lofominhili.farmflow.dto.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lofominhili.farmflow.utils.Measure;

public record StatisticByUserResponseDTO(
        @JsonProperty(value = "product_name")
        String productName,

        Integer amount,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Measure measure
) {
}
