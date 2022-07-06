package com.flab.commerce.user;

import com.flab.commerce.user.dto.LoginDto;
import com.flab.commerce.user.dto.RegisterDto;
import com.flab.commerce.user.validator.LoginDtoValidator;
import com.flab.commerce.user.validator.RegisterDtoValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    public static final String USER_EMAIL = "USER_EMAIL";

    private final UserService userService;
    private final RegisterDtoValidator registerDtoValidator;
    private final LoginDtoValidator loginDtoValidator;


    @InitBinder("registerDto")
    public void initRegisterDto(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(registerDtoValidator);
    }

    @InitBinder("loginDto")
    public void initLoginDto(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(loginDtoValidator);
    }


    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        User newUser = UserObjectMapper.INSTANCE.userToRegisterDto(registerDto);
        userService.register(newUser);

    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginDto loginDto, HttpSession session) {
        session.setAttribute(USER_EMAIL, loginDto.getEmail());
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute(USER_EMAIL);
    }
}
