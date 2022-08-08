package com.flab.commerce.domain.owner;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerObjectMapper {

  OwnerObjectMapper INSTANCE = Mappers.getMapper(OwnerObjectMapper.class);

  @Mapping(target = "password", expression = "java(com.flab.commerce.util.Utils.PASSWORD_ENCODER."
      + "encode(ownerRegisterDto.getPassword()))")
  Owner toOwner(OwnerRegisterDto ownerRegisterDto);
}
