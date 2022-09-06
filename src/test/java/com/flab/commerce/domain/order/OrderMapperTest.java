package com.flab.commerce.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyLong;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;
=======
import com.flab.commerce.domain.user.User;
import com.flab.commerce.domain.user.UserMapper;
import java.math.BigInteger;
import java.time.ZonedDateTime;
>>>>>>> 1c56d19... #40 주문에 옵션추가
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.dao.DataIntegrityViolationException;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderMapperTest {

  @Autowired
  private OrderMapper mapper;

  @Autowired
  UserMapper userMapper;

  @Test
<<<<<<< HEAD
  void 주문을_생성한다() {
    Orders order = Orders.builder()
        .address("서울시 서초구")
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .totalPrice(new BigDecimal(1000L))
        .menuOptions("{}")
        .userId(1L)
=======
  void 저장한다_1() {
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
    userMapper.insertUser(user);

    Orders order = Orders.builder()
        .totalPrice(BigInteger.valueOf(1000L))
        .status(OrderStatus.READY)
        .menuOptions(
            "[{\"name\":\"메뉴\",\"price\":1000,\"totalPrice\":10100,\"amount\":10,\"orderMenuOptionSaveDtos\":[{\"optionGroupName\":\"옵션그룹\",\"optionName\":\"옵션1\",\"price\":0},{\"optionGroupName\":\"옵션그룹\",\"optionName\":\"옵션2\",\"price\":10}]}]")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .userId(user.getId())
>>>>>>> 1c56d19... #40 주문에 옵션추가
        .build();

    // When
    int countInsert = mapper.save(order);
    Orders foundOrder = mapper.findById(order.getId());

    // Then
    assertThat(countInsert).isOne();
    assertThat(foundOrder.getTotalPrice()).isEqualTo(order.getTotalPrice());
    assertThat(foundOrder.getStatus()).isEqualTo(order.getStatus());
    assertThat(foundOrder.getMenuOptions()).isEqualTo(order.getMenuOptions());
    assertThat(foundOrder.getUserId()).isEqualTo(order.getUserId());
  }

  @Test
  void 저장한다_dataIntegrityViolationException_유저를찾을수없는경우() {
    // Given
    Orders order = Orders.builder()
        .totalPrice(BigInteger.valueOf(1000L))
        .status(OrderStatus.READY)
        .menuOptions(
            "[{\"name\":\"메뉴\",\"price\":1000,\"totalPrice\":10100,\"amount\":10,\"orderMenuOptionSaveDtos\":[{\"optionGroupName\":\"옵션그룹\",\"optionName\":\"옵션1\",\"price\":0},{\"optionGroupName\":\"옵션그룹\",\"optionName\":\"옵션2\",\"price\":10}]}]")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .userId(anyLong())
        .build();

    // When
    Exception exception = catchException(() -> mapper.save(order));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }
}
