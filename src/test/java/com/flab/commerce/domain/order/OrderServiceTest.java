package com.flab.commerce.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 상품을 주문한다
 * - 존재하는 메뉴인지 검증한다
 * - 주문은 가격을 가진다
 *  - 가격은 음수가 될 수 없다
 *  - 가격은 0이 될 수 없다
 * - 주문은 가격을 가진다
 * - 배송은 구매자를 가진다
 * - 배송은 주소를 가진다
 *  - 주소는 null이나 빈 값일 수 없다
 * - 배송은 상세주소를 가진다
 *  - 상세주소는 null이나 빈 값일 수 없다
 * - 배송은 우편번호를 가진다
 *  - 상세주소는 null이나 빈 값일 수 없다
 * - 배송은 전화번호를 가진다
 *  - 전화번호는 10 혹은 11자리의 숫자로 이루어져있다
 *  - 문자 혹은 특수문자를 포함할 수 없다
 * - 배송은 배송 상태를 가진다
 *  - 주문시 배송상태는 READY이다
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Test
  void 주문을_저장한다() throws JsonProcessingException {
    OrderSaveDto saveDto = OrderSaveDto.builder().build();
    Orders order = orderService.save(saveDto);
    assertThat(order).isNotNull();
  }
}
