package com.flab.commerce.domain.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;

class PriceMapperTest {

  @Autowired
  private PriceMapper mapper;

  void 가격을_저장한다() {
    Price pay = Price.builder()
        .price(BigInteger.valueOf(1_000L))
        .orderId(1L)
        .build();

    int countInsert = mapper.save(pay);

    assertThat(countInsert).isEqualTo(1);
  }
}
