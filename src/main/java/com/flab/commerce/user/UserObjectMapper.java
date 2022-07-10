package com.flab.commerce.user;

import com.flab.commerce.user.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserObjectMapper {

  UserObjectMapper INSTANCE = Mappers.getMapper(UserObjectMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createDateTime", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "modifyDateTime", expression = "java(java.time.LocalDateTime.now())")
  User registerDtoToUser(RegisterDto registerDto);
}
