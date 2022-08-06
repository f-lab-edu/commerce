package com.flab.commerce.domain.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PriceMapperTest {

  @Autowired
  private PriceMapper mapper;

  @Test
  void 가격을_저장한다() {
    Price pay = Price.builder()
        .price(BigDecimal.valueOf(1_000L))
        .orderId(1L)
        .build();

    int countInsert = mapper.save(pay);

    assertThat(countInsert).isEqualTo(1);
  }
}
