package com.flab.commerce.user;

import com.flab.commerce.mapper.UserMapper;
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
