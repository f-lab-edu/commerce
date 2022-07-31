package com.flab.commerce.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.flab.commerce.domain.user.User;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.domain.user.UserService;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  UserService userService;
  @Mock
  UserMapper userMapper;

  @Test
  void 회원가입_성공() {
    // Given
    User user = User.builder()
        .email("test@gmail.com")
        .name("홍길동")
        .zipcode("00000")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("1234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    given(userMapper.insertUser(user)).willReturn(1);

    // When
    boolean register = userService.register(user);

    // Then
    assertThat(register).isTrue();
  }

  @Test
  void 회원가입_실패() {
    // Given
    User user = User.builder()
        .email("test@gmail.com")
        .name("홍길동")
        .zipcode("00000")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("1234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    given(userMapper.insertUser(user)).willReturn(0);

    // When
    boolean register = userService.register(user);

    // Then
    assertThat(register).isFalse();
  }
}