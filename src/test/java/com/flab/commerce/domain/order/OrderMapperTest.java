package com.flab.commerce.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderMapperTest {

  @Autowired
  private OrderMapper mapper;

  @Test
  void 주문을_생성한다() {
    Orders order = Orders.builder()
        .address("서울시 서초구")
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .totalPrice(new BigDecimal(1000L))
        .menuOptions("{}")
        .userId(1L)
        .build();

    int countInsert = mapper.save(order);

    assertThat(countInsert).isEqualTo(1);
  }
}
