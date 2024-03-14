package com.lofominhili.farmflow.dto.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lofominhili.farmflow.utils.Measure;

import java.util.List;

public record StatisticByFarmResponseDTO(
        @JsonProperty(value = "product_name")
        String productName,
        List<String> email,
        Integer amount,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Measure measure
) {
}
