package com.flab.commerce.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.mapper.UserMapper;
import com.flab.commerce.user.dto.LoginDto;
import com.flab.commerce.user.dto.RegisterDto;
import com.flab.commerce.user.validator.LoginDtoValidator;
import com.flab.commerce.user.validator.RegisterDtoValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.flab.commerce.user.UserController.USER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({RegisterDtoValidator.class, LoginDtoValidator.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;
    @MockBean
    UserMapper userMapper;

    MockHttpSession session;

    @Autowired
    WebApplicationContext context;


    RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        registerDto = RegisterDto.builder()
                .email("teset@gmail.com")
                .name("홍길동")
                .zipcode("00000")
                .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
                .phone("010-1234-5678")
                .password("1234")
                .confirmPassword("1234")
                .build();
    }

    @Test
    void 회원가입_성공() throws Exception {
        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void 회원가입_실패_이메일_중복() throws Exception {

        when(userMapper.existEmail("teset@gmail.com")).thenReturn(true);

        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_패스워드와_확인패스워드가_다름() throws Exception {
        registerDto.setConfirmPassword("1235");
        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void 회원가입_실패_우편번호_길이_5_초과() throws Exception {
        registerDto.setZipcode("123456");
        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_우편번호_길이_2_미만() throws Exception {
        registerDto.setZipcode("12");

        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void 로그인_성공() throws Exception {
        LoginDto loginDto = new LoginDto("email@email.com", "1234");
        User user = UserObjectMapper.INSTANCE.userToRegisterDto(registerDto);
        when(userMapper.findByEmail(loginDto.getEmail())).thenReturn(user);
        session = new MockHttpSession();

        mockMvc.perform(post("/user/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
                ).andDo(print())
                .andExpect(status().isOk());
        String sessionEmail = (String) session.getAttribute(USER_EMAIL);
        assertNotNull(sessionEmail);
        assertNotNull(sessionEmail, loginDto.getEmail());
        assertNotNull(sessionEmail, user.getEmail());
    }

    @Test
    void 로그인_실패() throws Exception {
        LoginDto loginDto = new LoginDto("email@email.com", "1234");
        when(userMapper.findByEmail(loginDto.getEmail())).thenReturn(null);
        session = new MockHttpSession();

        mockMvc.perform(post("/user/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
                ).andDo(print())
                .andExpect(status().isBadRequest());
        String sessionEmail = (String) session.getAttribute(USER_EMAIL);
        assertNull(sessionEmail);
    }

}