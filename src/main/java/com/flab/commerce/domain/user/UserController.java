package com.flab.commerce.domain.user;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final RegisterDtoValidator registerDtoValidator;

  @InitBinder("registerDto")
  public void initRegisterDto(WebDataBinder webDataBinder) {
    webDataBinder.addValidators(registerDtoValidator);
  }

  @PostMapping
  public ResponseEntity<List<ObjectError>> register(@Valid @RequestBody RegisterDto registerDto,
      Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
    }

    User newUser = UserObjectMapper.INSTANCE.registerDtoToUser(registerDto);

    if (!userService.register(newUser)) {
      errors.reject(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "서버 오류");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors.getAllErrors());
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}