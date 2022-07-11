package com.flab.commerce.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.mapper.UserMapper;
import com.flab.commerce.security.RestUserDetailService;
import com.flab.commerce.user.dto.LoginDto;
import com.flab.commerce.user.dto.RegisterDto;
import com.flab.commerce.user.validator.RegisterDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = UserController.class)
@Import({RegisterDtoValidator.class, RestUserDetailService.class})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  UserService userService;
  @MockBean
  UserMapper userMapper;
  final String zipcode = "12345";
  final String phone = "010-1234-5678";

  @Test
  void 회원가입_성공() throws Exception {

    RegisterDto registerDto = getRegisterDto(zipcode, phone);
    String json = objectMapper.writeValueAsString(registerDto);
    when(userService.register(any())).thenReturn(true);

    mockMvc.perform(post("/user/register")
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

    mockMvc.perform(post("/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void 회원가입_실패_우편번호_길이_5_초과() throws Exception {
    RegisterDto registerDto = getRegisterDto("123456", phone);
    String json = objectMapper.writeValueAsString(registerDto);

    mockMvc.perform(post("/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void 회원가입_실패_우편번호_길이_2_미만() throws Exception {
    RegisterDto registerDto = getRegisterDto("12", phone);
    String json = objectMapper.writeValueAsString(registerDto);

    mockMvc.perform(post("/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void 회원가입_실패_휴대폰_길이_초과() throws Exception {
    RegisterDto registerDto = getRegisterDto(zipcode, "010-1234-567890");
    String json = objectMapper.writeValueAsString(registerDto);

    mockMvc.perform(post("/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void 회원가입_실패_휴대폰_길이_미만() throws Exception {
    RegisterDto registerDto = getRegisterDto(zipcode, "010-1234-567");
    String json = objectMapper.writeValueAsString(registerDto);

    mockMvc.perform(post("/user/register")
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

    mockMvc.perform(post("/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void 로그인_성공() throws Exception {
    RegisterDto registerDto = getRegisterDto(zipcode, phone);
    User user = UserObjectMapper.INSTANCE.registerDtoToUser(registerDto);

    LoginDto loginDto = new LoginDto("email@email.com", "12345678");
    when(userMapper.findByEmail(loginDto.getEmail())).thenReturn(user);
    MockHttpSession session = new MockHttpSession();

    mockMvc.perform(post("/user/login")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto))
        ).andDo(print())
        .andExpect(status().isOk());

    assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNotNull();
  }

  @Test
  void 로그인_실패() throws Exception {
    LoginDto loginDto = new LoginDto("email@email.com", "1234");
    when(userMapper.findByEmail(loginDto.getEmail())).thenReturn(null);
    MockHttpSession session = new MockHttpSession();

    mockMvc.perform(post("/user/login")
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

    mockMvc.perform(get("/user/logout")
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