package com.lofominhili.farmflow.mappers;

import com.lofominhili.farmflow.dto.UserDTO;
import com.lofominhili.farmflow.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "role", constant = "USER")
    UserEntity toEntity(UserDTO userDTO);

    UserDTO toDto(UserEntity user);

}
