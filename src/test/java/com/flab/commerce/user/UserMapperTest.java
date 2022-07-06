package com.flab.commerce.user;

import com.flab.commerce.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;


import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    UserMapper userMapper;
    User user;

    @BeforeEach
    void setUp() {
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
    void 사용자생성_성공() {
        int countInsertRow = userMapper.insertUser(user);
        assertEquals(1, countInsertRow);
    }

    @Test
    void 사용자생성_실패_이메일_중복() {
        userMapper.insertUser(user);
        assertThrows(DuplicateKeyException.class, () -> userMapper.insertUser(user));
    }

    @Test
    void 사용자조회_1_건() {
        userMapper.insertUser(user);

        User findUser = userMapper.findByEmail(user.getEmail());

        assertNotNull(findUser);
        assertEquals(user.getEmail(), findUser.getEmail());
    }

    @Test
    void 사용자조회_0_건() {
        User findUser = userMapper.findByEmail(user.getEmail());
        assertNull(findUser);
    }
}