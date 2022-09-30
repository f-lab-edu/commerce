package com.flab.commerce.domain.option;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import java.math.BigInteger;
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
        .price(BigInteger.valueOf(1000))
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
        .price(BigInteger.valueOf(1000))
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
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(AccessDeniedException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

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
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(BadInputException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

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

  @Test
  void 옵션수정_200() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService).updateOption(any());
  }

  @Test
  @WithMockUser
  void 옵션수정_403_사용자권한() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  @WithAnonymousUser
  void 옵션수정_403_익명사용자() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션수정_400_가게가존재하지않는경우() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(BadInputException.class).when(storeService)
        .validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션수정_401_가게의사장님이아닌경우() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(AccessDeniedException.class).when(storeService)
        .validateOwnerStore(any(), any());

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션수정_400_옵션그룹이존재하지않는경우() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(BadInputException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션수정_401_다른가게의옵션그룹인경우() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(AccessDeniedException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션수정_400_옵션이존재하지않는경우() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(BadInputException.class).when(optionService)
        .validate(any(), any());

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션수정_401_옵션그룹의옵션이아닌경우() throws Exception {
    // Given
    OptionUpdateDto dto = OptionUpdateDto.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .build();
    String body = objectMapper.writeValueAsString(dto);

    // When
    doThrow(AccessDeniedException.class).when(optionService)
        .validate(any(), any());

    // Then
    mockMvc.perform(put("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService, never()).updateOption(any());
  }

  @Test
  void 옵션삭제_200() throws Exception {
    // When
    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService).deleteOption(any());
  }

  @Test
  @WithMockUser
  void 옵션삭제_403_사용자권한() throws Exception {
    // When
    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  @WithAnonymousUser
  void 옵션삭제_403_익명사용자권한() throws Exception {
    // When
    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void 옵션삭제_400_가게가존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService)
        .validateOwnerStore(any(), any());

    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void 옵션삭제_401_가게의사장님이아닌경우() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(storeService)
        .validateOwnerStore(any(), any());

    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void 옵션삭제_400_옵션그룹이존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void 옵션삭제_401_다른가게의옵션그룹인경우() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void 옵션삭제_400_옵션이존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(optionService)
        .validate(any(), any());

    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void 옵션삭제_401_옵션그룹의옵션이아닌경우() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(optionService)
        .validate(any(), any());

    mockMvc.perform(delete("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService, never()).deleteOption(any());
  }

  @Test
  void getOption_200() throws Exception {
    // When
    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService).getOption(any());
  }

  @Test
  @WithMockUser
  void getOption_403_사용자권한() throws Exception {
    // When
    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).getOption(any());
  }

  @Test
  @WithAnonymousUser
  void getOption_403_익명사용자권한() throws Exception {
    // When
    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());

    verify(storeService, never()).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).getOption(any());
  }

  @Test
  void getOption_400_가게가존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(storeService)
        .validateOwnerStore(any(), any());

    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).getOption(any());
  }

  @Test
  void getOption_401_가게의사장님이아닌경우() throws Exception {
    // When
    doThrow(AccessDeniedException.class).when(storeService)
        .validateOwnerStore(any(), any());

    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService, never()).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).getOption(any());
  }

  @Test
  void getOption_400_옵션그룹이존재하지않는경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(optionGroupService)
        .validateOptionGroupStore(any(), any());

    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService, never()).validate(4L, 3L);
    verify(optionService, never()).getOption(any());
  }

  @Test
  void getOption_400_다른가게의옵션그룹인경우() throws Exception {
    // When
    doThrow(BadInputException.class).when(optionService).validate(any(), any());

    mockMvc.perform(get("/stores/2/option-groups/3/options/4")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(storeService).validateOwnerStore(1L, 2L);
    verify(optionGroupService).validateOptionGroupStore(3L, 2L);
    verify(optionService).validate(4L, 3L);
    verify(optionService, never()).getOption(any());
  }
}