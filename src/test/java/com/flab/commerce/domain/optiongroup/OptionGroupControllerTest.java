package com.flab.commerce.domain.optiongroup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

@WebMvcTest(OptionGroupController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(role = Constants.ROLE_OWNER)
class OptionGroupControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  OptionGroupService optionGroupService;

  @MockBean
  StoreService storeService;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @Test
  void 옵션그룹등록_200() throws Exception {
    // Given
    OptionGroupRegisterDto optionGroupRegisterDto = OptionGroupRegisterDto.builder()
        .name("옵션그룹명")
        .build();
    String body = objectMapper.writeValueAsString(optionGroupRegisterDto);

    // Then
    mockMvc.perform(post("/stores/51/option-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).registerOptionGroup(any());
  }

  @Test
  @WithMockUser
  void 옵션그룹등록_403_사용자권한() throws Exception {
    // Given
    OptionGroupRegisterDto optionGroupRegisterDto = OptionGroupRegisterDto.builder()
        .name("옵션그룹명")
        .build();
    String body = objectMapper.writeValueAsString(optionGroupRegisterDto);

    // Then
    mockMvc.perform(post("/stores/51/option-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).registerOptionGroup(any());
  }

  @Test
  @WithAnonymousUser
  void 옵션그룹등록_403_익명사용자() throws Exception {
    // Given
    OptionGroupRegisterDto optionGroupRegisterDto = OptionGroupRegisterDto.builder()
        .name("옵션그룹명")
        .build();
    String body = objectMapper.writeValueAsString(optionGroupRegisterDto);

    // Then
    mockMvc.perform(post("/stores/51/option-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).registerOptionGroup(any());
  }

  @Test
  void 옵션그룹등록_400_가게가존재하지않는경우() throws Exception {
    // Given
    OptionGroupRegisterDto optionGroupRegisterDto = OptionGroupRegisterDto.builder()
        .name("옵션그룹명")
        .build();
    String body = objectMapper.writeValueAsString(optionGroupRegisterDto);

    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(post("/stores/51/option-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).registerOptionGroup(any());
  }

  @Test
  void 옵션그룹등록_401_사장님의가게가아닌경우() throws Exception {
    // Given
    OptionGroupRegisterDto optionGroupRegisterDto = OptionGroupRegisterDto.builder()
        .name("옵션그룹명")
        .build();
    String body = objectMapper.writeValueAsString(optionGroupRegisterDto);

    doThrow(AccessDeniedException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(post("/stores/51/option-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).registerOptionGroup(any());
  }

  @Test
  void 옵션그룹들조회_200() throws Exception {
    // Given
    List<OptionGroup> optionGroups = Arrays.asList(
        OptionGroup.builder().id(1L).name("옵션그룹1").storeId(1L).createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now()).build(),
        OptionGroup.builder().id(2L).name("옵션그룹2").storeId(1L).createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now()).build(),
        OptionGroup.builder().id(3L).name("옵션그룹3").createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now()).build());

    // When
    when(optionGroupService.getOptionGroups(any())).thenReturn(optionGroups);

    // Then
    mockMvc.perform(get("/stores/1/option-groups")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).getOptionGroups(any());
  }

  @Test
  void 옵션그룹들조회_200_empty_가게의옵션그룹이없는경우() throws Exception {
    // Given
    List<OptionGroup> emptyList = Collections.emptyList();

    // When
    when(optionGroupService.getOptionGroups(any())).thenReturn(emptyList);

    // Then
    mockMvc.perform(get("/stores/1/option-groups")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).getOptionGroups(any());
  }

  @Test
  @WithMockUser
  void 옵션그룹들조회_403_사용자권한() throws Exception {
    // Then
    mockMvc.perform(get("/stores/1/option-groups")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).getOptionGroups(any());
  }

  @Test
  @WithAnonymousUser
  void 옵션그룹들조회_403_익명사용자권한() throws Exception {
    // Then
    mockMvc.perform(get("/stores/1/option-groups")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).getOptionGroups(any());
  }

  @Test
  void 옵션그룹들조회_400_가게가없는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(get("/stores/1/option-groups")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).getOptionGroups(any());
  }

  @Test
  void 옵션그룹들조회_401_다른사장님의가게() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(get("/stores/1/option-groups")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).getOptionGroups(any());
  }

  @Test
  void 옵션그룹삭제_200() throws Exception {

    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).validateOptionGroupStore(any(), any());
    verify(optionGroupService).deleteOptionGroup(any());
  }

  @Test
  @WithMockUser
  void 옵션그룹삭제_403_사용자권한() throws Exception {

    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).validateOptionGroupStore(any(), any());
    verify(optionGroupService, never()).deleteOptionGroup(any());
  }

  @Test
  @WithAnonymousUser
  void 옵션그룹삭제_403_익명사용자권한() throws Exception {

    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).validateOptionGroupStore(any(), any());
    verify(optionGroupService, never()).deleteOptionGroup(any());
  }

  @Test
  void 옵션그룹삭제_400_가게가없는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).validateOptionGroupStore(any(), any());
    verify(optionGroupService, never()).deleteOptionGroup(any());
  }

  @Test
  void 옵션그룹삭제_401_다른사장님의가게() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).validateOptionGroupStore(any(), any());
    verify(optionGroupService, never()).deleteOptionGroup(any());
  }

  @Test
  void 옵션그룹삭제_400_옵션그룹이없는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(optionGroupService).validateOptionGroupStore(any(), any());

    // Then
    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).validateOptionGroupStore(any(), any());
    verify(optionGroupService, never()).deleteOptionGroup(any());
  }

  @Test
  void 옵션그룹삭제_400_다른가게의옵션그룹일경우() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(optionGroupService).deleteOptionGroup(any());

    // Then
    mockMvc.perform(delete("/stores/1/option-groups/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).validateOptionGroupStore(any(), any());
    verify(optionGroupService).deleteOptionGroup(any());
  }
}