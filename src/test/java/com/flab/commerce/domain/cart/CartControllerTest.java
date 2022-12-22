package com.flab.commerce.domain.cart;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.menu.MenuMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@Import(CartValidator.class)
@WebMvcTest(value = CartController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(role = Constants.ROLE_USER)
class CartControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  CartService cartService;

  @MockBean
  UserMapper userMapper;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  MenuMapper menuMapper;

  @Test
  void 메뉴등록_201() throws Exception {
    // Given
    long menuId = 1L;
    CartRegisterDto cartRegisterDto = CartRegisterDto.builder()
        .amount(1L)
        .menuId(menuId)
        .build();
    String body = objectMapper.writeValueAsString(cartRegisterDto);

    // When
    when(menuMapper.idExists(menuId)).thenReturn(true);
    mockMvc.perform(post("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    // Then
    verify(cartService).addMenu(any());
  }

  @Test
  void 메뉴등록_400_메뉴가존재하지않는경우() throws Exception {
    // Given
    long menuId = 1L;
    CartRegisterDto cartRegisterDto = CartRegisterDto.builder()
        .amount(1L)
        .menuId(menuId)
        .build();
    String body = objectMapper.writeValueAsString(cartRegisterDto);

    // When
    when(menuMapper.idExists(menuId)).thenReturn(false);
    mockMvc.perform(post("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(cartService, never()).addMenu(any());
  }

  @Test
  void 메뉴등록_400_수량이0이하() throws Exception {
    // Given
    long menuId = 1L;
    CartRegisterDto cartRegisterDto = CartRegisterDto.builder()
        .amount(0L)
        .menuId(menuId)
        .build();
    String body = objectMapper.writeValueAsString(cartRegisterDto);

    // When
    when(menuMapper.idExists(menuId)).thenReturn(true);
    mockMvc.perform(post("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(cartService, never()).addMenu(any());
  }

  @Test
  @WithAnonymousUser
  void 메뉴등록_403_익명사용자() throws Exception {
    // Given
    CartRegisterDto cartRegisterDto = CartRegisterDto.builder()
        .amount(1L)
        .menuId(1L)
        .build();
    String body = objectMapper.writeValueAsString(cartRegisterDto);

    // When
    mockMvc.perform(post("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).addMenu(any());
    verify(menuMapper, never()).idExists(anyLong());
  }

  @Test
  @WithMockDetails(role = Constants.ROLE_OWNER)
  void 메뉴등록_403_사장님() throws Exception {
    // Given
    CartRegisterDto cartRegisterDto = CartRegisterDto.builder()
        .amount(1L)
        .menuId(1L)
        .build();
    String body = objectMapper.writeValueAsString(cartRegisterDto);

    // When
    mockMvc.perform(post("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).addMenu(any());
    verify(menuMapper, never()).idExists(anyLong());
  }

  @Test
  void 메뉴수정_200() throws Exception {
    // Given
    long id = 1L;
    long amount = 2L;
    CartUpdateDto cartUpdateDto = CartUpdateDto.builder()
        .id(id)
        .amount(amount)
        .build();
    String body = objectMapper.writeValueAsString(cartUpdateDto);

    // When
    mockMvc.perform(put("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    // Then
    verify(cartService).updateAmount(any());
  }

  @Test
  void 메뉴수정_400_수량이0이하() throws Exception {
    // Given
    long id = 1L;
    long amount = 0L;
    CartUpdateDto cartUpdateDto = CartUpdateDto.builder()
        .id(id)
        .amount(amount)
        .build();
    String body = objectMapper.writeValueAsString(cartUpdateDto);

    // When
    mockMvc.perform(put("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(cartService, never()).updateAmount(any());
  }

  @Test
  void 수량수정_401_다른사용자의장바구니() throws Exception {
    // Given
    long id = 1L;
    long amount = 2L;
    CartUpdateDto cartUpdateDto = CartUpdateDto.builder()
        .id(id)
        .amount(amount)
        .build();
    String body = objectMapper.writeValueAsString(cartUpdateDto);

    // When
    doThrow(AccessDeniedException.class).when(cartService).updateAmount(any());

    mockMvc.perform(put("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    // Then
    verify(cartService).updateAmount(any());
  }

  @Test
  @WithAnonymousUser
  void 메뉴수정_403_익명사용자() throws Exception {
    // Given
    long id = 1L;
    long amount = 2L;
    CartUpdateDto cartUpdateDto = CartUpdateDto.builder()
        .id(id)
        .amount(amount)
        .build();
    String body = objectMapper.writeValueAsString(cartUpdateDto);

    // When
    mockMvc.perform(put("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).updateAmount(any());
  }

  @Test
  @WithMockDetails(role = Constants.ROLE_OWNER)
  void 메뉴수정_403_사장님() throws Exception {
    // Given
    long id = 1L;
    long amount = 2L;
    CartUpdateDto cartUpdateDto = CartUpdateDto.builder()
        .id(id)
        .amount(amount)
        .build();
    String body = objectMapper.writeValueAsString(cartUpdateDto);

    // When
    mockMvc.perform(put("/carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).updateAmount(any());
  }

  @Test
  void 삭제_200() throws Exception {
    // Given
    long id = 1L;

    // When
    mockMvc.perform(delete("/carts/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    // Then
    verify(cartService).deleteById(anyLong(), anyLong());
  }

  @Test
  void 삭제_401_다른사용자의장바구니() throws Exception {
    // Given
    long id = 1L;

    // When
    doThrow(AccessDeniedException.class).when(cartService).deleteById(anyLong(), anyLong());

    mockMvc.perform(delete("/carts/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    // Then
    verify(cartService).deleteById(anyLong(), anyLong());
  }

  @Test
  @WithAnonymousUser
  void 삭제_403_익명사용자() throws Exception {
    // Given
    long id = 1L;

    // When
    mockMvc.perform(delete("/carts/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).deleteById(anyLong(), anyLong());
  }

  @Test
  @WithMockDetails(role = Constants.ROLE_OWNER)
  void 삭제_403_사장님() throws Exception {
    // Given
    long id = 1L;

    // When
    mockMvc.perform(delete("/carts/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).deleteById(anyLong(), anyLong());
  }

  @Test
  void 사용자ID로삭제_200() throws Exception {
    // When
    mockMvc.perform(delete("/carts")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    // Then
    verify(cartService).deleteByUserId(anyLong());
  }

  @Test
  @WithAnonymousUser
  void 사용자ID로삭제_403_익명사용자() throws Exception {
    // When
    mockMvc.perform(delete("/carts")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).deleteByUserId(anyLong());
  }

  @Test
  @WithMockDetails(role = Constants.ROLE_OWNER)
  void 사용자ID로삭제_403_사장님() throws Exception {
    // When
    mockMvc.perform(delete("/carts")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(cartService, never()).deleteByUserId(anyLong());
  }
}