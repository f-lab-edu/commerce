package com.flab.commerce.domain.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.exception.BadInputException;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MenuController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(rule = Constants.ROLE_OWNER)
class MenuControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  MenuService menuService;

  @MockBean
  StoreService storeService;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @Test
  void 메뉴삭제_200() throws Exception {
    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(any(), any());
    verify(menuService).validateMenu(any(), any());
    verify(menuService).deleteMenu(any(), any());
  }

  @Test
  @WithMockUser
  void 메뉴삭제_403_사용자권한() throws Exception {
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(menuService, never()).validateMenu(any(), any());
    verify(menuService, never()).deleteMenu(any(), any());
  }

  @Test
  @WithAnonymousUser
  void 메뉴삭제_403_익명사용자() throws Exception {
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(menuService, never()).validateMenu(any(), any());
    verify(menuService, never()).deleteMenu(any(), any());
  }

  @Test
  void 메뉴삭제_400_가게id가존재하지않을경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(menuService, never()).validateMenu(any(), any());
    verify(menuService, never()).deleteMenu(any(), any());
  }

  @Test
  void 메뉴삭제_401_다른사장님의가게() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(any(), any());
    verify(menuService, never()).validateMenu(any(), any());
    verify(menuService, never()).deleteMenu(any(), any());
  }

  @Test
  void 메뉴삭제_400_메뉴id가존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(menuService).validateMenu(any(), any());
    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(menuService).validateMenu(any(), any());
    verify(menuService, never()).deleteMenu(any(), any());
  }

  @Test
  void 메뉴삭제_400_가게의메뉴가아닌경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(menuService).validateMenu(any(), any());
    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(menuService).validateMenu(any(), any());
    verify(menuService, never()).deleteMenu(any(), any());
  }
}
