package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.commerce.exception.BadInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

  @InjectMocks
  private MenuService menuService;

  @Mock
  private MenuMapper menuMapper;

  @Test
  void 메뉴삭제_void() {
    // Given
    when(menuMapper.deleteByIdAndStoreId(any(), any())).thenReturn(1);
    // When
    menuService.deleteMenu(any(), any());
    // Then
    verify(menuMapper).deleteByIdAndStoreId(any(), any());
  }

  @Test
  void 메뉴삭제_DataIntegrityViolationException_삭제처리못한경우() {
    // When
    doThrow(DataIntegrityViolationException.class).when(menuMapper)
        .deleteByIdAndStoreId(any(), any());
    Throwable throwable = catchThrowableOfType(
        () -> menuService.deleteMenu(1L, 1L), DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 메뉴유효성검사_void() {
    // When
    when(menuMapper.idExists(any())).thenReturn(true);
    when(menuMapper.idAndStoreIdExists(any(), any())).thenReturn(true);

    menuService.validateMenu(1L, 1L);

    // Then
    verify(menuMapper).idExists(any());
    verify(menuMapper).idAndStoreIdExists(any(), any());
  }

  @Test
  void 메뉴유효성검사_BadInputException_id가존재하지않음() {
    // When
    when(menuMapper.idExists(any())).thenReturn(false);

    Throwable throwable = catchThrowableOfType((() -> menuService.validateMenu(1L, 1L)),
        BadInputException.class);

    // Then
    assertThat(throwable).isInstanceOf(BadInputException.class);
    verify(menuMapper).idExists(any());
    verify(menuMapper, never()).idAndStoreIdExists(any(), any());
  }

  @Test
  void 메뉴유효성검사_void_id와가게id가존재하지않음() {
    // When
    when(menuMapper.idExists(any())).thenReturn(true);
    when(menuMapper.idAndStoreIdExists(any(), any())).thenReturn(false);

    Throwable throwable = catchThrowableOfType((() -> menuService.validateMenu(1L, 1L)),
        AccessDeniedException.class);

    // Then
    assertThat(throwable).isInstanceOf(AccessDeniedException.class);
    verify(menuMapper).idExists(any());
    verify(menuMapper).idAndStoreIdExists(any(), any());
  }
}