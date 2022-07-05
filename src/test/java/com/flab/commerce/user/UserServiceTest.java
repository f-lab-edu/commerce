package com.flab.commerce.user;

import com.flab.commerce.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserMapper userMapper;
    User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .email("teset@gmail.com")
                .name("홍길동")
                .zipcode("00000")
                .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
                .phone("010-1234-5678")
                .password("1234")
                .build();
    }

    @Test
    void 회원가입_성공() {
        given(userMapper.insertUser(user)).willReturn(1);
        assertEquals(true, userService.register(user));
    }
}