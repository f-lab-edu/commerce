package com.flab.commerce.domain.option;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionObjectMapper {

  OptionObjectMapper INSTANCE = Mappers.getMapper(OptionObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Option toEntity(OptionRegisterDto optionRegisterDto, Long optionGroupId);

  @Mapping(target = "createDateTime", ignore = true)
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  Option toEntity(OptionUpdateDto optionUpdateDto, Long id, Long optionGroupId);

  OptionResponseDto toDto(Option option);
}
