package com.flab.commerce.domain.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.user.User;
import com.flab.commerce.domain.user.UserController;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.domain.user.UserObjectMapper;
import com.flab.commerce.domain.user.UserService;
import com.flab.commerce.domain.user.LoginDto;
import com.flab.commerce.domain.user.RegisterDto;
import com.flab.commerce.domain.user.RegisterDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = UserController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@Import(RegisterDtoValidator.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  UserService userService;

  @MockBean
  UserMapper userMapper;

  @MockBean
  OwnerMapper ownerMapper;

  final String zipcode = "12345";

  final String phone = "010-1234-5678";

  @Test
  void 회원가입_성공() throws Exception {
    RegisterDto registerDto = getRegisterDto(zipcode, phone);
    String json = objectMapper.writeValueAsString(registerDto);

    when(userService.register(any())).thenReturn(true);

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  void 회원가입_실패_이메일_중복() throws Exception {
    when(userMapper.emailExists("test@gmail.com")).thenReturn(true);
    RegisterDto registerDto = getRegisterDto(zipcode, phone);
    String json = objectMapper.writeValueAsString(registerDto);

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }


  @Test
  void 회원가입_실패_서버에러() throws Exception {
    when(userService.register(any())).thenReturn(false);
    RegisterDto registerDto = getRegisterDto(zipcode, phone);

    String json = objectMapper.writeValueAsString(registerDto);

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void 로그인_성공() throws Exception {
    RegisterDto registerDto = getRegisterDto(zipcode, phone);
    User user = UserObjectMapper.INSTANCE.registerDtoToUser(registerDto);

    LoginDto loginDto = new LoginDto("email@email.com", "12345678", "/users/login");
    when(userMapper.findByEmail(loginDto.getEmail())).thenReturn(user);
    MockHttpSession session = new MockHttpSession();

    mockMvc.perform(post("/users/login")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto))
        ).andDo(print())
        .andExpect(status().isOk());

    assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNotNull();
  }

  @Test
  void 로그인_실패() throws Exception {
    LoginDto loginDto = new LoginDto("email@email.com", "12345678", "/users/login");
    when(userMapper.findByEmail(loginDto.getEmail())).thenReturn(null);
    MockHttpSession session = new MockHttpSession();

    mockMvc.perform(post("/users/login")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto))
        ).andDo(print())
        .andExpect(status().isUnauthorized());

    assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNull();
  }

  @Test
  void 로그아웃_성공() throws Exception {
    MockHttpSession session = new MockHttpSession();

    mockMvc.perform(get("/users/logout")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());

    assertThatThrownBy(()->session.getAttribute("SPRING_SECURITY_CONTEXT")).isInstanceOf(IllegalStateException.class);
  }

  private RegisterDto getRegisterDto(String zipcode, String phone) {
    return RegisterDto.builder()
        .email("test@gmail.com")
        .name("홍길동")
        .zipcode(zipcode)
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone(phone)
        .password("12345678")
        .build();
  }
}