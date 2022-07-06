package com.flab.commerce.user.validator;

import com.flab.commerce.mapper.UserMapper;
import com.flab.commerce.user.User;
import com.flab.commerce.user.dto.LoginDto;
import com.flab.commerce.user.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class LoginDtoValidator implements Validator {

    private final UserMapper userMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(LoginDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDto loginDto = (LoginDto) target;
        User user = userMapper.findByEmail(loginDto.getEmail());
        boolean result = false;
        if (user == null || !user.getPassword().equals(loginDto.getPassword())) result = true;

        if (result) errors.reject("아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.");

    }
}
