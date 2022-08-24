package com.flab.commerce.domain.menuoptiongroup;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuOptionGroupObjectMapper {

  MenuOptionGroupObjectMapper INSTANCE = Mappers.getMapper(MenuOptionGroupObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "menus", ignore = true)
  @Mapping(target = "optionGroup", ignore = true)
  @Mapping(target = "options", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  MenuOptionGroup toEntity(Long menuId, Long optionGroupId);

  @Mapping(target = "optionGroupName", source = "optionGroup.name")
  @Mapping(target = "menuNames", expression = "java(menuOptionGroup.getMenus().stream()"
      + ".map(com.flab.commerce.domain.menu.Menu::getName).collect("
      + "java.util.stream.Collectors.joining(\",\")))")
  MenuOptionGroupResponseDto toDto(MenuOptionGroup menuOptionGroup);
}
