package com.flab.commerce.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.commerce.mapper.UserMapper;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

  @Autowired
  UserMapper userMapper;

  @Test
  void 사용자생성_성공() {
    User user = getUser();

    int countInsertRow = userMapper.insertUser(user);

    assertThat(countInsertRow).isOne();
  }

  @Test
  void 사용자생성_실패_이메일_중복() {
    User user = getUser();

    userMapper.insertUser(user);

    assertThatThrownBy(() -> userMapper.insertUser(user)).isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void 사용자조회_1_건() {
    User user = getUser();
    userMapper.insertUser(user);

    User findUser = userMapper.findByEmail(user.getEmail());

    assertThat(findUser).isNotNull();
    assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  void 사용자조회_0_건() {
    User user = getUser();

    User findUser = userMapper.findByEmail(user.getEmail());

    assertThat(findUser).isNull();
  }

  private User getUser() {
    return User.builder()
        .email("test@gmail.com")
        .name("홍길동")
        .zipcode("00000")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("1234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
  }
}