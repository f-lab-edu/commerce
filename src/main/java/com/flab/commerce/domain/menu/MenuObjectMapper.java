package com.flab.commerce.domain.menu;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuObjectMapper {

  MenuObjectMapper INSTANCE = Mappers.getMapper(MenuObjectMapper.class);

  List<SearchMenuDto> toDto(List<Menu> menus);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "groups", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Menu toEntity(MenuRegisterDto menuRegisterDto, Long storeId);


  @Mapping(target = "storeId", ignore = true)
  @Mapping(target = "groups", ignore = true)
  @Mapping(target = "createDateTime", ignore = true)
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Menu toEntity(MenuPatchDto menuPatchDto, Long id);
}
