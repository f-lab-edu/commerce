package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        .price(10000L)
        .image("image")
        .storeId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(menuMapper.register(menu)).thenReturn(1);

    boolean successfulRegister = menuService.registerMenu(menu);

    // Then
    assertThat(successfulRegister).isTrue();
  }

  @Test
  void 메뉴등록_실패_서버에러() {
    // Given
    Menu menu = Menu.builder()
        .name("돈까스")
        .price(10000L)
        .image("image")
        .storeId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(menuMapper.register(menu)).thenReturn(0);

    boolean successfulRegister = menuService.registerMenu(menu);

    // Then
    assertThat(successfulRegister).isFalse();
  }
}