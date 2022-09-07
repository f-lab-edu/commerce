package com.flab.commerce.domain.order;

import static com.flab.commerce.util.Constants.JSON_PROCESSING_EXCEPTION_MESSAGE;
import static com.flab.commerce.util.Constants.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.order.OrderSaveDto.OrderSaveDtoBuilder;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.security.WithMockDetails;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(rule = ROLE_USER)
class OrderControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @MockBean
  private OrderService orderService;

  @Test
  void 주문한다() throws Exception {
    // Given
    String body = mapper.writeValueAsString(createOrderSaveDto().build());

    // When
    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    // Then
    verify(orderService).save(anyLong(), any(OrderSaveDto.class));
  }

  @Test
  void JSON_파싱에_실패한_경우() throws Exception {
    // Given
    String body = mapper.writeValueAsString(createOrderSaveDto().build());

    // When
    doThrow(JsonProcessingException.class).when(orderService)
        .save(anyLong(), any(OrderSaveDto.class));

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(
            JSON_PROCESSING_EXCEPTION_MESSAGE));

    // Then
    verify(orderService).save(anyLong(), any(OrderSaveDto.class));
  }

  @ParameterizedTest
  @MethodSource("nullOrEmpty")
  void 빈_주소로_주문한다(String address) throws Exception {
    String body = mapper.writeValueAsString(createOrderSaveDto().address(address).build());

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @MethodSource("nullOrEmpty")
  void 빈_상세_주소로_주문한다(String detailAddress) throws Exception {
    String body = mapper.writeValueAsString(
        createOrderSaveDto().addressDetail(detailAddress).build());

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @NullSource
  void 빈_메뉴_아이디로_주문한다(Long menuId) throws Exception {
    OrderSaveDto orderSaveDto = createOrderSaveDto().build();
    orderSaveDto.getMenus().add(OrderMenuDto.builder()
        .menuId(menuId)
        .amount(1)
        .optionIds(Arrays.asList(2L, 3L))
        .build());
    String body = mapper.writeValueAsString(orderSaveDto);

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(ints = {Integer.MIN_VALUE, -1})
  void 음수_메뉴_개수로_주문한다(int amount) throws Exception {
    OrderSaveDto orderSaveDto = createOrderSaveDto().build();
    orderSaveDto.getMenus().add(OrderMenuDto.builder()
        .menuId(5L)
        .amount(amount)
        .optionIds(Collections.singletonList(6L))
        .build());
    String body = mapper.writeValueAsString(orderSaveDto);

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  private OrderSaveDtoBuilder createOrderSaveDto() {
    List<OrderMenuDto> orderMenuDtos = new LinkedList<>();
    orderMenuDtos.add(OrderMenuDto.builder()
        .menuId(1L)
        .amount(1)
        .optionIds(Arrays.asList(2L, 3L))
        .build());

    return OrderSaveDto.builder()
        .menus(orderMenuDtos)
        .address("서울시 서초구")
        .addressDetail("201")
        .phone("01045808682")
        .zipCode("12345");
  }

  public static Stream<String> nullOrEmpty() {
    return Stream.of(null, "", " ");
  }
}
