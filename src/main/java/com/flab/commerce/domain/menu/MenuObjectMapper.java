package com.flab.commerce.domain.menu;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuObjectMapper {

  MenuObjectMapper INSTANCE = Mappers.getMapper(MenuObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Menu toEntity(MenuRegisterDto menuRegisterDto, Long storeId);
}
