package com.flab.commerce.user;

import com.flab.commerce.user.dto.RegisterDto;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegisterDtoValidator registerDtoValidator;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(registerDtoValidator);
    }

    @PostMapping("/user/register")
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        User newUser = UserObjectMapper.INSTANCE.userToRegisterDto(registerDto);
        userService.register(newUser);
    }
}
