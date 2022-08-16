package com.flab.commerce.domain.option;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.optiongroup.OptionGroupService;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.StoreService;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.exception.BadInputException;
import com.flab.commerce.security.WithMockDetails;
import com.flab.commerce.util.Constants;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OptionController.class)
@ComponentScan(basePackages = "com.flab.commerce.security")
@WithMockDetails(role = Constants.ROLE_OWNER)
class OptionControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  OptionGroupService optionGroupService;

  @MockBean
  OptionService optionService;

  @MockBean
  StoreService storeService;

  @MockBean
  OwnerMapper ownerMapper;

  @MockBean
  UserMapper userMapper;

  @Test
  void 옵션등록_200() throws Exception {
    // Given
    OptionRegisterDto dto = OptionRegisterDto.builder()
        .name("옵션1")
        .price(BigDecimal.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // Then
    mockMvc.perform(
            post("/stores/1/option-groups/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).validateOptionGroupStore(any(), any());
    verify(optionService).registerOption(any());
  }

  @Test
  void 옵션등록_400_가게id가존재하지않을경우() throws Exception {
    // Given
    OptionRegisterDto dto = OptionRegisterDto.builder()
        .name("옵션1")
        .price(BigDecimal.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(BadInputException.class).when(storeService).validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(
            post("/stores/1/option-groups/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService, never()).validateOptionGroupStore(any(), any());
    verify(optionService, never()).registerOption(any());
  }

  @Test
  void 옵션등록_401_다른사장님의가게() throws Exception {
    // Given
    OptionRegisterDto dto = OptionRegisterDto.builder()
        .name("옵션1")
        .price(BigDecimal.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(AccessDeniedException.class).when(optionGroupService).validateOptionGroupStore(any(), any());

    // Then
    mockMvc.perform(
            post("/stores/1/option-groups/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).validateOptionGroupStore(any(), any());
    verify(optionService, never()).registerOption(any());
  }

  @Test
  void 옵션등록_400_옵션그룹이존재하지않다() throws Exception {
    // Given
    OptionRegisterDto dto = OptionRegisterDto.builder()
        .name("옵션1")
        .price(BigDecimal.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(BadInputException.class).when(optionGroupService).validateOptionGroupStore(any(), any());

    // Then
    mockMvc.perform(
            post("/stores/1/option-groups/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(any(), any());
    verify(optionGroupService).validateOptionGroupStore(any(), any());
    verify(optionService, never()).registerOption(any());
  }
}