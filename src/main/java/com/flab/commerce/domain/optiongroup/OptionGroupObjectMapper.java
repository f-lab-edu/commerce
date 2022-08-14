package com.flab.commerce.domain.optiongroup;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionGroupObjectMapper {

  OptionGroupObjectMapper INSTANCE = Mappers.getMapper(OptionGroupObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  OptionGroup toEntity(OptionGroupRegisterDto optionGroupRegisterDto, Long storeId);

  List<OptionGroupReadDto> toDto(List<OptionGroup> optionGroups);
}
