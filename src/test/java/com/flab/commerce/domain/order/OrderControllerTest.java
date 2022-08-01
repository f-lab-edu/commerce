package com.flab.commerce.domain.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.order.OrderSaveDto.OrderSaveDtoBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private OrderService orderService;

  @Test
  void 주문한다() throws Exception {
    String body = mapper.writeValueAsString(createOrderSaveDto().build());

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());
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
    String body = mapper.writeValueAsString(createOrderSaveDto().addressDetail(detailAddress).build());

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @NullSource
  void 빈_메뉴_아이디로_주문한다(Long menuId) throws Exception {
    String body = mapper.writeValueAsString(createOrderSaveDto().menuId(menuId).build());

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @NullSource
  void 빈_사용자_아이디로_주문한다(Long userId) throws Exception {
    String body = mapper.writeValueAsString(createOrderSaveDto().userId(userId).build());

    mvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  private OrderSaveDtoBuilder createOrderSaveDto() {
    return OrderSaveDto.builder()
        .address("서울시 서초구")
        .addressDetail("201")
        .phone("01045808682")
        .amount(3)
        .userId(1L)
        .menuId(1L)
        .zipCode("12345");
  }

  public static Stream<String> nullOrEmpty() {
    return Stream.of(null, "", " ");
  }
}
