package com.flab.commerce.domain.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.exception.BadInputException;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
@WithMockDetails(role = Constants.ROLE_OWNER)
class MenuControllerTest {

  @Autowired
  MockMvc mockMvc;

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

  @MockBean
  MenuMapper menuMapper;

  void 메뉴등록_201() throws Exception {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("메뉴1")
        .price(1000L)
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(post("/stores/0/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).registerMenu(any());
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
    doThrow(AccessDeniedException.class).when(storeService)
        .validateOwnerStore(anyLong(), anyLong());

    // Then
    mockMvc.perform(post("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).registerMenu(any());
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
    doThrow(BadInputException.class).when(storeService)
        .validateOwnerStore(anyLong(), anyLong());
    // Then
    mockMvc.perform(post("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).registerMenu(any());
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
    verify(storeService, never()).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).registerMenu(any());
  }

  @Test
  @WithAnonymousUser
  void 메뉴목록조회_200_익명사용자() throws Exception {
    // Given
    List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu(), new Menu());

    // When
    when(menuService.getMenus(anyLong())).thenReturn(menus);

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStoreExistence(anyLong());
    verify(menuService).getMenus(anyLong());
  }

  @Test
  @WithMockUser(authorities = Constants.ROLE_USER)
  void 메뉴목록조회_200_사용자() throws Exception {
    // Given
    List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu(), new Menu());

    // When
    when(menuService.getMenus(anyLong())).thenReturn(menus);

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStoreExistence(anyLong());
    verify(menuService).getMenus(anyLong());
  }

  @Test
  @WithMockUser(authorities = Constants.ROLE_OWNER)
  void 메뉴목록조회_200_사장님() throws Exception {
    // Given
    List<Menu> menus = Arrays.asList(new Menu(), new Menu(), new Menu(), new Menu());

    // When
    when(menuService.getMenus(anyLong())).thenReturn(menus);

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStoreExistence(anyLong());
    verify(menuService).getMenus(anyLong());
  }

  @Test
  void 메뉴목록조회_200_가게에메뉴가없는경우() throws Exception {
    // When
    when(menuService.getMenus(anyLong())).thenReturn(Collections.emptyList());

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateStoreExistence(anyLong());
    verify(menuService).getMenus(anyLong());
  }

  @Test
  void 메뉴목록조회_400_가게가없는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateStoreExistence(anyLong());

    // Then
    mockMvc.perform(get("/stores/51/menus")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateStoreExistence(anyLong());
    verify(menuService, never()).getMenus(anyLong());
  }

  @Test
  void 메뉴삭제_200() throws Exception {
    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).validateMenu(anyLong(), anyLong());
    verify(menuService).deleteMenu(anyLong(), anyLong());
  }

  @Test
  @WithMockUser
  void 메뉴삭제_403_사용자권한() throws Exception {
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).deleteMenu(anyLong(), anyLong());
  }

  @Test
  @WithAnonymousUser
  void 메뉴삭제_403_익명사용자() throws Exception {
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).deleteMenu(anyLong(), anyLong());
  }

  @Test
  void 메뉴삭제_400_가게id가존재하지않을경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(anyLong(), anyLong());

    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).deleteMenu(anyLong(), anyLong());
  }

  @Test
  void 메뉴삭제_401_다른사장님의가게() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(storeService)
        .validateOwnerStore(anyLong(), anyLong());

    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).deleteMenu(anyLong(), anyLong());
  }

  @Test
  void 메뉴삭제_400_메뉴id가존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(menuService).validateMenu(anyLong(), anyLong());
    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).deleteMenu(anyLong(), anyLong());
  }

  @Test
  void 메뉴삭제_401_가게의메뉴가아닌경우() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(menuService).validateMenu(anyLong(), anyLong());
    // Then
    mockMvc.perform(delete("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).deleteMenu(anyLong(), anyLong());
  }

  @Test
  void 메뉴패치_200() throws Exception {
    // Given
    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).validateMenu(anyLong(), anyLong());
    verify(menuService).patchMenu(any());
  }

  @Test
  @WithMockUser
  void 메뉴패치_403_사용자권한() throws Exception {
    // Given
    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).patchMenu(any());
  }

  @Test
  @WithAnonymousUser
  void 메뉴패치_403_익명사용자() throws Exception {
    // Given
    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).patchMenu(any());
  }

  @Test
  void 메뉴패치_400_가게id가존재하지않을경우() throws Exception {
    // Given
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(anyLong(), anyLong());

    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).patchMenu(any());
  }

  @Test
  void 메뉴패치_401_다른사장님의가게() throws Exception {
    // Given
    doThrow(AccessDeniedException.class).when(storeService)
        .validateOwnerStore(anyLong(), anyLong());

    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService, never()).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).patchMenu(any());
  }

  @Test
  void 메뉴패치_400_메뉴id가존재하지않는경우() throws Exception {
    // Given
    doThrow(BadInputException.class).when(menuService).validateMenu(anyLong(), anyLong());

    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).patchMenu(any());
  }

  @Test
  void 메뉴패치_401_가게의메뉴가아닌경우() throws Exception {
    // Given
    doThrow(AccessDeniedException.class).when(menuService).validateMenu(anyLong(), anyLong());

    MenuPatchDto menuRegisterDto = MenuPatchDto.builder()
        .name("메뉴2")
        .price(BigInteger.valueOf(1000L))
        .image(UUID.randomUUID() + ".jpg")
        .build();
    String body = objectMapper.writeValueAsString(menuRegisterDto);

    // Then
    mockMvc.perform(patch("/stores/51/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(anyLong(), anyLong());
    verify(menuService).validateMenu(anyLong(), anyLong());
    verify(menuService, never()).patchMenu(any());
  }
}
