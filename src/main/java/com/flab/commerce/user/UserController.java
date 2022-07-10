package com.flab.commerce.user;

import com.flab.commerce.user.dto.LoginDto;
import com.flab.commerce.user.dto.RegisterDto;
import com.flab.commerce.user.validator.LoginDtoValidator;
import com.flab.commerce.user.validator.RegisterDtoValidator;
import com.flab.commerce.util.Constants;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

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
    User newUser = UserObjectMapper.INSTANCE.registerDtoToUser(registerDto);
    userService.register(newUser);
  }

  @PostMapping("/login")
  public void login(@Valid @RequestBody LoginDto loginDto, HttpSession session) {
    session.setAttribute(Constants.USER_EMAIL, loginDto.getEmail());
  }

  @GetMapping("/logout")
  public void logout(HttpSession session) {
    session.removeAttribute(Constants.USER_EMAIL);
  }
}
