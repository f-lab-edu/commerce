package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
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
}