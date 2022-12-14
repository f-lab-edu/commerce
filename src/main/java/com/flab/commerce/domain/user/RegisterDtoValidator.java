package com.flab.commerce.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class RegisterDtoValidator implements Validator {

  private final UserMapper userMapper;

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(RegisterDto.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RegisterDto registerDto = (RegisterDto) target;

    if (userMapper.emailExists(registerDto.getEmail())) {
      errors.rejectValue("email", "email.exists", "이미 존재하는 이메일입니다.");
    }
  }
}
