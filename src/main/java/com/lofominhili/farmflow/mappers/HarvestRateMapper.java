package com.lofominhili.farmflow.mappers;

import com.lofominhili.farmflow.dto.HarvestRateDTO;
import com.lofominhili.farmflow.entities.HarvestRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HarvestRateMapper {

    HarvestRateEntity toEntity(HarvestRateDTO harvestRateDTO);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.measure", target = "measure")
    @Mapping(source = "amount", target = "left")
    HarvestRateDTO toDto(HarvestRateEntity harvestRate);

}
