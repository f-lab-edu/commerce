package com.flab.commerce.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @InjectMocks
  private CartService cartService;

  @Mock
  private CartMapper cartMapper;

  @Test
  void 상품추가_상품이존재할경우() {
    // Given
    long addAmount = 1L;
    long amount = 1L;
    Cart cart = Cart.builder()
        .amount(addAmount)
        .userId(1L)
        .menuId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    Cart foundCart = Cart.builder().amount(amount).build();

    // When
    when(cartMapper.userIdAndMenuIdExists(cart.getUserId(), cart.getMenuId())).thenReturn(true);
    when(cartMapper.findByUserIdAndMenuId(cart.getUserId(), cart.getMenuId())).thenReturn(
        foundCart);
    cartService.addMenu(cart);

    // Then
    verify(cartMapper).findByUserIdAndMenuId(cart.getUserId(), cart.getMenuId());
    verify(cartMapper).updateAmount(foundCart);
    verify(cartMapper, never()).register(any());
    assertThat(foundCart.getAmount()).isEqualTo(amount + addAmount);
  }

  @Test
  void 상품추가_상품이존재하지않는경우() {
    // Given
    Cart cart = Cart.builder()
        .amount(1L)
        .userId(1L)
        .menuId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(cartMapper.userIdAndMenuIdExists(any(), any())).thenReturn(false);
    cartService.addMenu(cart);

    // Then
    verify(cartMapper, never()).findByUserIdAndMenuId(any(), any());
    verify(cartMapper, never()).updateAmount(any());
    verify(cartMapper).register(cart);
  }

  @Test
  void 상품수정() {
    // Given
    Long id = 1L;
    Long amount = 2L;
    Long userId = 3L;
    Cart cart = Cart.builder()
        .id(id)
        .amount(amount)
        .userId(userId)
        .modifyDateTime(ZonedDateTime.now())
        .build();
    Cart foundCart = Cart.builder().id(id).userId(userId).build();

    // When
    when(cartMapper.findById(id)).thenReturn(foundCart);
    cartService.updateAmount(cart);

    // Then
    verify(cartMapper).findById(id);
    verify(cartMapper).updateAmount(any());
  }

  @Test
  void 상품수정_상품이없는경우() {
    // Given
    Long id = 1L;
    Long amount = 2L;
    Long userId = 3L;
    Cart cart = Cart.builder()
        .id(id)
        .amount(amount)
        .userId(userId)
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(cartMapper.findById(id)).thenReturn(null);
    cartService.updateAmount(cart);

    // Then
    verify(cartMapper).findById(id);
    verify(cartMapper, never()).updateAmount(any());
  }

  @Test
  void 상품수정_다른사용자의장바구니상품일경우() {
    // Given
    Long id = 1L;
    Long amount = 2L;
    Long userId = 3L;
    Long otherUserId = 4L;
    Cart cart = Cart.builder()
        .id(id)
        .amount(amount)
        .userId(userId)
        .modifyDateTime(ZonedDateTime.now())
        .build();
    Cart foundCart = Cart.builder().id(id).userId(otherUserId).build();

    // When
    when(cartMapper.findById(id)).thenReturn(foundCart);
    Exception exception = catchException(() -> cartService.updateAmount(cart));

    // Then
    assertThat(exception).isInstanceOf(AccessDeniedException.class);
    verify(cartMapper).findById(id);
    verify(cartMapper, never()).updateAmount(any());
  }

  @Test
  void 삭제() {
    // Given
    Long id = 1L;
    Long userId = 2L;
    Cart cart = Cart.builder().id(id).userId(userId).build();

    // When
    when(cartMapper.findById(id)).thenReturn(cart);
    cartService.deleteById(id, userId);

    // Then
    verify(cartMapper).findById(id);
    verify(cartMapper).deleteById(id);
  }

  @Test
  void 삭제_상품이없는경우() {
    // Given
    Long id = 1L;
    Long userId = 2L;

    // When
    when(cartMapper.findById(id)).thenReturn(null);
    cartService.deleteById(id, userId);

    // Then
    verify(cartMapper).findById(id);
    verify(cartMapper, never()).deleteById(id);
  }

  @Test
  void 삭제_다른사용자의장바구니() {
    // Given
    Long id = 1L;
    Long userId = 2L;
    Long otherUserId = 3L;
    Cart cart = Cart.builder().id(id).userId(otherUserId).build();

    // When
    when(cartMapper.findById(id)).thenReturn(cart);
    Exception exception = catchException(() -> cartService.deleteById(id, userId));

    // Then
    assertThat(exception).isInstanceOf(AccessDeniedException.class);
    verify(cartMapper).findById(id);
    verify(cartMapper, never()).deleteById(id);
  }

  @Test
  void 사용자ID로삭제() {
    // When
    cartService.deleteByUserId(1L);

    // Then
    verify(cartMapper).deleteByUserId(anyLong());
  }
}