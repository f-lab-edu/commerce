package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.commerce.exception.BadInputException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
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
  void 메뉴등록_성공() {
    // Given
    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    menuService.registerMenu(menu);

    // Then
    verify(menuMapper).register(menu);
  }

  @Test
  void 메뉴등록_DataIntegrityViolationException_존재하지않는가게ID() {
    // Given
    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .storeId(-1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(menuMapper.register(menu)).thenThrow(DataIntegrityViolationException.class);
    Throwable throwable = catchThrowableOfType(() -> menuService.registerMenu(menu),
        DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 메뉴목록조회_성공() {
    // Given
    when(menuMapper.findByStoreId(any())).thenReturn(
        Collections.singletonList(Menu.builder().build()));
    // When
    List<Menu> menus = menuService.getMenus(1L);
    // Then
    assertThat(menus).hasSize(1);
  }

  @Test
  void 메뉴목록조회_empty_가게id가null() {
    // Given
    when(menuMapper.findByStoreId(any())).thenReturn(Collections.emptyList());
    // When
    List<Menu> menus = menuService.getMenus(null);
    // Then
    assertThat(menus).isEmpty();
  }

  @Test
  void 메뉴목록조회_empty_가게에메뉴가없는경우() {
    // Given
    when(menuMapper.findByStoreId(any())).thenReturn(Collections.emptyList());
    // When
    List<Menu> menus = menuService.getMenus(1L);
    // Then
    assertThat(menus).isEmpty();
  }

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

  @Test
  void 메뉴패치_void() {
    // Given
    when(menuMapper.patch(any())).thenReturn(1);
    // When
    menuService.patchMenu(any());
    // Then
    verify(menuMapper).patch(any());
  }

  @Test
  void 메뉴패치_DataIntegrityViolationException_삭제처리못한경우() {
    // Given
    Menu patchMenu = Menu.builder()
        .id(-1L)
        .name("치즈 돈까스")
        .price(BigInteger.valueOf(10001L))
        .image("image2")
        .modifyDateTime(ZonedDateTime.now())
        .build();
    // When
    doThrow(DataIntegrityViolationException.class).when(menuMapper).patch(any());
    Throwable throwable = catchThrowableOfType(
        () -> menuService.patchMenu(patchMenu), DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }
}