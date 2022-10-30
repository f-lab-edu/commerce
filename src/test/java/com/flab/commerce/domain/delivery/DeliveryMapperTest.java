package com.flab.commerce.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.flab.commerce.domain.order.OrderMapper;
import com.flab.commerce.domain.order.OrderStatus;
import com.flab.commerce.domain.order.Orders;
import com.flab.commerce.domain.user.User;
import com.flab.commerce.domain.user.UserMapper;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MybatisTest
class DeliveryMapperTest {

  @Autowired
  private DeliveryMapper mapper;

  @Autowired
  OrderMapper orderMapper;

  @Autowired
  UserMapper userMapper;

  @Test
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
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .totalPrice(BigInteger.valueOf(1000L))
        .menuOptions(
            "[{\"name\":\"메뉴\",\"price\":1000,\"totalPrice\":10100,\"amount\":10,\"orderMenuOptionSaveDtos\":[{\"optionGroupName\":\"옵션그룹\",\"optionName\":\"옵션1\",\"price\":0},{\"optionGroupName\":\"옵션그룹\",\"optionName\":\"옵션2\",\"price\":10}]}]")
        .status(OrderStatus.READY)
        .userId(user.getId())
        .build();
    orderMapper.save(order);

    Delivery delivery = Delivery.builder()
        .address("서울시 서초구")
        .addressDetail("201호")
        .zipcode("12345")
        .phone("01045808682")
        .status(DeliveryStatus.READY)
        .orderId(order.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int countInsert = mapper.save(delivery);
    Delivery foundDelivery = mapper.findById(delivery.getId());

    // Then
    assertThat(countInsert).isEqualTo(1);
    assertThat(foundDelivery.getAddress()).isEqualTo(delivery.getAddress());
    assertThat(foundDelivery.getAddressDetail()).isEqualTo(delivery.getAddressDetail());
    assertThat(foundDelivery.getZipcode()).isEqualTo(delivery.getZipcode());
    assertThat(foundDelivery.getPhone()).isEqualTo(delivery.getPhone());
    assertThat(foundDelivery.getStatus()).isEqualTo(delivery.getStatus());
    assertThat(foundDelivery.getOrderId()).isEqualTo(delivery.getOrderId());
    assertThat(foundDelivery.getRiderId()).isEqualTo(delivery.getRiderId());
  }

  @Test
  void 저장한다_dataIntegrityViolationException_주문을찾을수없는경우() {
    // Given
    Delivery delivery = Delivery.builder()
        .address("서울시 서초구")
        .addressDetail("201호")
        .zipcode("12345")
        .phone("01045808682")
        .status(DeliveryStatus.READY)
        .orderId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    Exception exception = catchException(()->mapper.save(delivery));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }
}
