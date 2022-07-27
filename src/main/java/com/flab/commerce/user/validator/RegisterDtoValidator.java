package com.flab.commerce.user.validator;

import com.flab.commerce.mapper.UserMapper;
import com.flab.commerce.user.dto.RegisterDto;
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
      errors.rejectValue("email", "이미 존재하는 이메일입니다.");
    }
  }
}
