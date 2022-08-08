package com.flab.commerce.domain.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserObjectMapper {

  UserObjectMapper INSTANCE = Mappers.getMapper(UserObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.ZonedDateTime.now())")
  @Mapping(target = "password", expression = "java(com.flab.commerce.util.Utils.PASSWORD_ENCODER."
      + "encode(registerDto.getPassword()))")
  User registerDtoToUser(RegisterDto registerDto);
}
