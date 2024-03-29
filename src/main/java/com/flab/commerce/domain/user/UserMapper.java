package com.flab.commerce.domain.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  boolean emailExists(String email);

  int insertUser(User newUser);

  User findByEmail(String email);
}