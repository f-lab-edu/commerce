package com.flab.commerce.domain.optiongroup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
}