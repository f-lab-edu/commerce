package com.flab.commerce.user;

import com.flab.commerce.user.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserObjectMapper {
    UserObjectMapper INSTANCE = Mappers.getMapper(UserObjectMapper.class);
    User userToRegisterDto(RegisterDto registerDto);
}
