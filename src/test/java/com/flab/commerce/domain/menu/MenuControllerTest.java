package com.flab.commerce.domain.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.exception.BadInputException;
import com.flab.commerce.util.Constants;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MenuController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
class MenuControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  StoreService storeService;

  @MockBean
  MenuService menuService;

  @MockBean
  MenuMapper menuMapper;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @Test
  @WithAnonymousUser
  void 메뉴목록조회_200_익명사용자() throws Exception {
    // Given
    List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu(), new Menu());

    // When
    when(menuService.getMenus(any())).thenReturn(menus);

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStore(any());
    verify(menuService).getMenus(any());
  }

  @Test
  @WithMockUser(authorities = Constants.ROLE_USER)
  void 메뉴목록조회_200_사용자() throws Exception {
    // Given
    List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu(), new Menu());

    // When
    when(menuService.getMenus(any())).thenReturn(menus);

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStore(any());
    verify(menuService).getMenus(any());
  }

  @Test
  @WithMockUser(authorities = Constants.ROLE_OWNER)
  void 메뉴목록조회_200_사장님() throws Exception {
    // Given
    List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu(), new Menu());

    // When
    when(menuService.getMenus(any())).thenReturn(menus);

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStore(any());
    verify(menuService).getMenus(any());
  }

  @Test
  void 메뉴목록조회_200_가게에메뉴가없는경우() throws Exception {
    // When
    when(menuService.getMenus(any())).thenReturn(Collections.emptyList());

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStore(any());
    verify(menuService).getMenus(any());
  }

  @Test
  void 메뉴목록조회_400_가게가없는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateStore(any());

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateStore(any());
    verify(menuService, never()).getMenus(any());
  }
}
