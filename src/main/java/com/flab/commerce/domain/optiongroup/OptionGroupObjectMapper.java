package com.flab.commerce.domain.optiongroup;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionGroupObjectMapper {

  OptionGroupObjectMapper INSTANCE = Mappers.getMapper(OptionGroupObjectMapper.class);

  List<OptionGroupReadDto> toDto(List<OptionGroup> optionGroups);

  OptionGroupAndOptionsResponseDto toDto(OptionGroup optionGroup);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "options", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  OptionGroup toEntity(OptionGroupRegisterDto optionGroupRegisterDto, Long storeId);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "options", ignore = true)
  @Mapping(target = "createDateTime", ignore = true)
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  OptionGroup toEntity(OptionGroupUpdateDto optionGroupUpdateDto, Long storeId);
}
