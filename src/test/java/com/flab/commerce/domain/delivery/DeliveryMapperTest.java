package com.flab.commerce.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class DeliveryMapperTest {

  @Autowired
  private DeliveryMapper mapper;

  @Test
  void 배달을_저장한다() {
    Delivery delivery = Delivery.builder()
        .recipient("박병길")
        .address("서울시 서초구")
        .addressDetail("201호")
        .zip("12345")
        .phone("01045808682")
        .status(DeliveryStatus.READY)
        .orderId(1L)
        .riderId(1L)
        .build();

    int countInsert = mapper.save(delivery);

    assertThat(countInsert).isEqualTo(1);
  }
}
