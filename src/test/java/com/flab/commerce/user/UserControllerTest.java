package com.flab.commerce.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.mapper.UserMapper;
import com.flab.commerce.user.dto.RegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
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
    @DisplayName("회원가입 성공")
    public void register() throws Exception {
        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원가입_실패: 이메일 중복")
    public void register_fail_duplicate_email() throws Exception {

        when(userMapper.existEmail("teset@gmail.com")).thenReturn(true);

        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입_실패: 패스워드와 확인패스워드가 다름")
    public void register_fail_not_equals_password_and_confirmPassword() throws Exception {
        registerDto.setConfirmPassword("1235");
        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("회원가입_실패: 우편번호 길이 5 초과")
    public void register_fail_over_length_zipcode() throws Exception {
        registerDto.setZipcode("123456");
        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입_실패: 우편번호 길이 2미만")
    public void register_fail_under_length_zipcode() throws Exception {
        registerDto.setZipcode("12");

        String json = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}