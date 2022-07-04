package com.flab.commerce.user;

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

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "비밀번화와 확인 비밀번호가 일치하지 않습니다.");
        }
        if (userMapper.existEmail(registerDto.getEmail())) {
            errors.rejectValue("email", "이미 존재하는 이메일입니다.");
        }

    }
}
