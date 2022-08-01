package com.flab.commerce.domain.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.security.owner.OwnerDetailsService;
import com.flab.commerce.security.user.GeneralUserDetailsService;
import com.flab.commerce.util.Constants;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MenuController.class)
@Import({OwnerDetailsService.class, GeneralUserDetailsService.class})
@WithMockUser(roles = Constants.ROLE_OWNER)
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
  private OwnerMapper ownerMapper;

  @MockBean
  private UserMapper userMapper;

  @Test
  void 메뉴등록_성공() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .storeId(1L)
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // When
    when(menuService.registerMenu(any())).thenReturn(true);

    // Then
    mockMvc.perform(post("/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated());
  }

  @Test
  void 메뉴등록_실패_서버에러() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .storeId(1L)
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // When
    when(menuService.registerMenu(any())).thenReturn(false);

    // Then
    mockMvc.perform(post("/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isInternalServerError());
  }
}
