package com.flab.commerce.domain.menu;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuObjectMapper {

  MenuObjectMapper INSTANCE = Mappers.getMapper(MenuObjectMapper.class);

  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Menu toEntity(MenuPatchDto menuPatchDto, Long id);
}
