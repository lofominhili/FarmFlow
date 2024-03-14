package com.lofominhili.farmflow.mappers;

import com.lofominhili.farmflow.dto.ProductDTO;
import com.lofominhili.farmflow.entities.ProductEntity;
import com.lofominhili.farmflow.utils.Measure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    @Mapping(target = "measure", source = "measure", qualifiedByName = "toUpperCase")
    ProductEntity toEntity(ProductDTO productDTO);

    ProductDTO toDto(ProductEntity product);

    @Named(value = "toUpperCase")
    default Measure toUpperCase(String measure) {
        return Measure.valueOf(measure.toUpperCase());
    }
}
