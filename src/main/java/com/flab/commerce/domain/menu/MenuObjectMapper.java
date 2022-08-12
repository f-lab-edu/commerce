package com.flab.commerce.domain.menu;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuObjectMapper {

  MenuObjectMapper INSTANCE = Mappers.getMapper(MenuObjectMapper.class);

  List<SearchMenuDto> toDto(List<Menu> menus);
}
