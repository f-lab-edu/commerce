package com.flab.commerce.domain.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartObjectMapper {
  CartObjectMapper INSTANCE = Mappers.getMapper(CartObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Cart dtoToCart(CartRegisterDto cartRegisterDto, Long userId);
}
