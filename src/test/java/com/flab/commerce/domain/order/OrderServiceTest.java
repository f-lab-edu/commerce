package com.flab.commerce.domain.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.delivery.Delivery;
import com.flab.commerce.domain.delivery.DeliveryMapper;
import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuMapper;
import com.flab.commerce.domain.option.Option;
import com.flab.commerce.domain.option.OptionMapper;
import com.flab.commerce.domain.optiongroup.OptionGroup;
import com.flab.commerce.domain.optiongroup.OptionGroupMapper;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
  OrderService orderService;

  @Mock
  MenuMapper menuMapper;

  @Mock
  OptionMapper optionMapper;

  @Mock
  OptionGroupMapper optionGroupMapper;

  @Spy
  @Autowired
  ObjectMapper objectMapper;

  @Mock
  OrderMapper orderMapper;

  @Mock
  DeliveryMapper deliveryMapper;

  final long menuId = 1L;
  final long optionId = 2L;
  final long optionId2 = 3L;
  final long optionId3 = 4L;
  final long optionGroupId = 5L;

  @Test
  void 주문을_저장한다() throws JsonProcessingException {
    // Given

    OrderMenuDto orderMenuDto = OrderMenuDto.builder()
        .menuId(menuId)
        .amount(10)
        .optionIds(Arrays.asList(optionId, optionId2, optionId3))
        .build();

    List<OrderMenuDto> orderMenuDtos = Collections.singletonList(orderMenuDto);

    OrderSaveDto saveDto = OrderSaveDto.builder()
        .menus(orderMenuDtos)
        .address("주소")
        .addressDetail("상세주소")
        .zipCode("12345")
        .phone("010-1234-5678")
        .build();


    Menu menu = Menu.builder()
        .id(menuId)
        .name("메뉴")
        .price(BigInteger.valueOf(1000))
        .image(UUID.randomUUID() + ".png")
        .storeId(optionGroupId)
        .build();

    Option option = Option.builder()
        .id(optionId)
        .name("옵션1")
        .price(BigInteger.ZERO)
        .optionGroupId(optionGroupId)
        .build();

    Option option2 = Option.builder()
        .id(optionId2)
        .name("옵션2")
        .price(BigInteger.TEN)
        .optionGroupId(optionGroupId)
        .build();

    Option option3 = Option.builder()
        .id(optionId3)
        .name("옵션3")
        .price(BigInteger.valueOf(13L))
        .optionGroupId(optionGroupId)
        .build();

    OptionGroup optionGroup = OptionGroup.builder()
        .id(optionGroupId)
        .name("옵션그룹")
        .build();

    // When
    Set<Long> menuIds = Collections.singleton(menuId);
    when(menuMapper.findByIdIn(menuIds)).thenReturn(Collections.singletonList(menu));

    Set<Long> optionIds = new HashSet<>(Arrays.asList(optionId, optionId2, optionId3));
    when(optionMapper.findByIdIn(optionIds)).thenReturn(Arrays.asList(option, option2, option3));

    Set<Long> optionGroupIds = new HashSet<>(Collections.singletonList(optionGroupId));
    when(optionGroupMapper.findByIdIn(optionGroupIds)).thenReturn(
        Collections.singletonList(optionGroup));

    orderService.save(anyLong(), saveDto);

    // Then
    verify(menuMapper).findByIdIn(menuIds);
    verify(optionMapper).findByIdIn(optionIds);
    verify(optionGroupMapper).findByIdIn(optionGroupIds);
    verify(orderMapper).save(any(Orders.class));
    verify(deliveryMapper).save(any(Delivery.class));
  }
}
