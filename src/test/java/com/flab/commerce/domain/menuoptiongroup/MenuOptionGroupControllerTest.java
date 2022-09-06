package com.flab.commerce.domain.menuoptiongroup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.menu.MenuService;
import com.flab.commerce.domain.optiongroup.OptionGroupService;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.exception.BadInputException;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuOptionGroupController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(role = Constants.ROLE_OWNER)
class MenuOptionGroupControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @MockBean
  StoreService storeService;

  @MockBean
  MenuService menuService;

  @MockBean
  OptionGroupService optionGroupService;

  @MockBean
  MenuOptionGroupService menuOptionGroupService;

  final Long storeId = 1L;
  final Long menuId = 2L;
  final Long optionGroupId = 3L;
  final Long optionGroupId2 = 4L;

  @Test
  void 모든저장_200() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isCreated());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService).validateMenu(menuId, storeId);
    verify(optionGroupService).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  @WithMockUser
  void 모든저장_403_사용자권한() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(storeService, never()).validateOwnerStore(any(), eq(storeId));
    verify(menuService, never()).validateMenu(menuId, storeId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  @WithAnonymousUser
  void 모든저장_403_익명사용자권한() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isForbidden());

    // Then
    verify(storeService, never()).validateOwnerStore(any(), eq(storeId));
    verify(menuService, never()).validateMenu(menuId, storeId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_400_가게가존재하지않을경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), eq(storeId));
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService, never()).validateMenu(menuId, storeId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_401_다른사장님의가게인경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(AccessDeniedException.class).when(storeService).validateOwnerStore(any(), eq(storeId));
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isUnauthorized());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService, never()).validateMenu(menuId, storeId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_400_메뉴가존재하지않을경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(BadInputException.class).when(menuService).validateMenu(menuId, storeId);
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService).validateMenu(menuId, storeId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_401_다른가게의메뉴인경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(AccessDeniedException.class).when(menuService).validateMenu(menuId, storeId);
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isUnauthorized());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService).validateMenu(menuId, storeId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_400_옵션그룹이존재하지않는경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(BadInputException.class).when(optionGroupService)
        .validateOptionGroupStore(optionGroupId, storeId);
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService).validateMenu(menuId, storeId);
    verify(optionGroupService).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_401_다른가게의옵션그룹인경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(AccessDeniedException.class).when(optionGroupService)
        .validateOptionGroupStore(optionGroupId, storeId);
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isUnauthorized());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService).validateMenu(menuId, storeId);
    verify(optionGroupService).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 모든저장_400_이미존재하는메뉴옵션그룹인경우() throws Exception {
    // Given
    MenuOptionGroupRequestDto menuOptionGroupRequestDto = new MenuOptionGroupRequestDto(menuId,
        Arrays.asList(optionGroupId, optionGroupId2));
    String body = objectMapper.writeValueAsString(menuOptionGroupRequestDto);

    // When
    doThrow(BadInputException.class).when(menuOptionGroupService)
        .validateDuplicate(menuId, optionGroupId);
    mockMvc.perform(post("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON)
            .content(body)
        ).andDo(print())
        .andExpect(status().isBadRequest());

    // Then
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuService).validateMenu(menuId, storeId);
    verify(optionGroupService).validateOptionGroupStore(optionGroupId, storeId);
    verify(menuOptionGroupService).validateDuplicate(menuId, optionGroupId);
    verify(optionGroupService, never()).validateOptionGroupStore(optionGroupId2, storeId);
    verify(menuOptionGroupService, never()).validateDuplicate(menuId, optionGroupId2);
    verify(menuOptionGroupService, never()).saveAfterDeletion(eq(menuId), any());
  }

  @Test
  void 메뉴옵션그룹들가져오기_200() throws Exception {
    // Then
    mockMvc.perform(get("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuOptionGroupService).findByStoreId(storeId);
  }

  @Test
  @WithMockUser
  void 메뉴옵션그룹들가져오기_403_사용자권한() throws Exception {
    // Then
    mockMvc.perform(get("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());
    verify(storeService, never()).validateOwnerStore(any(), eq(storeId));
    verify(menuOptionGroupService, never()).findByStoreId(storeId);
  }

  @Test
  @WithAnonymousUser
  void 메뉴옵션그룹들가져오기_403_익명사용자권한() throws Exception {
    // Then
    mockMvc.perform(get("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());
    verify(storeService, never()).validateOwnerStore(any(), eq(storeId));
    verify(menuOptionGroupService, never()).findByStoreId(storeId);
  }

  @Test
  void 메뉴옵션그룹들가져오기_400_가게가존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), eq(storeId));
    // Then
    mockMvc.perform(get("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuOptionGroupService, never()).findByStoreId(storeId);
  }

  @Test
  void 메뉴옵션그룹들가져오기_401_다른사장님의가게인경우() throws Exception {
    // When
  doThrow(AccessDeniedException.class).when(storeService).validateOwnerStore(any(), eq(storeId));
    // Then
    mockMvc.perform(get("/stores/{storeId}/menu-option-groups", storeId)
            .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());
    verify(storeService).validateOwnerStore(any(), eq(storeId));
    verify(menuOptionGroupService, never()).findByStoreId(storeId);
  }
}