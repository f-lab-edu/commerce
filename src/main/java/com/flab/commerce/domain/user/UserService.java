package com.flab.commerce.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;

  public boolean register(User newUser) {
    return userMapper.insertUser(newUser) == 1;
  }
}
