package com.flab.commerce.domain.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MenuController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(rule = Constants.OWNER)
class MenuControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  MenuService menuService;

  @MockBean
  MenuMapper menuMapper;

  @MockBean
  StoreMapper storeMapper;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @Test
  void 메뉴등록_201() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // When
    when(storeMapper.idExists(any())).thenReturn(true);
    when(storeMapper.idAndOwnerIdExists(any(), any())).thenReturn(true);
    when(menuService.registerMenu(any(Menu.class))).thenReturn(true);

    // Then
    mockMvc.perform(post("/stores/0/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  void 메뉴등록_401_다른가게사장님() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // When
    when(storeMapper.idExists(any())).thenReturn(true);
    when(storeMapper.idAndOwnerIdExists(any(), any())).thenReturn(false);

    // Then
    mockMvc.perform(post("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void 메뉴등록_400_존재하지않는가게() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // When
    when(storeMapper.idExists(any())).thenReturn(false);

    // Then
    mockMvc.perform(post("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void 메뉴등록_400_메뉴정보검증실패() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(-1L)
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(post("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void 메뉴등록_500_서버에러() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // When
    when(storeMapper.idExists(any())).thenReturn(true);
    when(storeMapper.idAndOwnerIdExists(any(), any())).thenReturn(true);
    when(menuService.registerMenu(any(Menu.class))).thenReturn(false);

    // Then
    mockMvc.perform(post("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
