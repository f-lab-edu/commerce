package com.flab.commerce.domain.optiongroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.time.ZonedDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class OptionGroupServiceTest {

  @InjectMocks
  OptionGroupService optionGroupService;

  @Mock
  OptionGroupMapper optionGroupMapper;

  @Test
  void 옵션메뉴를등록_void() {
    // When
    optionGroupService.registerOptionGroup(any());

    // Then
    verify(optionGroupMapper).save(any());
  }

  @Test
  void 옵션메뉴를등록_DataIntegrityViolationException_NotNull예외() {
    // Given
    OptionGroup nameIsNull = OptionGroup.builder()
        .name(null)
        .storeId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    doThrow(DataIntegrityViolationException.class).when(optionGroupMapper).save(any());
    Throwable throwable = Assertions.catchThrowableOfType(
        () -> optionGroupService.registerOptionGroup(nameIsNull),
        DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    verify(optionGroupMapper).save(any());
  }
}