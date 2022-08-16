package com.flab.commerce.domain.option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.commerce.exception.BadInputException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

  @InjectMocks
  OptionService optionService;

  @Mock
  OptionMapper optionMapper;

  @Test
  void 옵션저장_void(){
    // When
    when(optionMapper.save(any())).thenReturn(1);
    optionService.registerOption(any());

    // Then
    verify(optionMapper).save(any());
  }

  @Test
  void 옵션저장_DataIntegrityViolationException_columnNotNull예외(){
    // Given
    Option nameIsNull = Option.builder()
        .name(null)
        .price(BigInteger.ZERO)
        .optionGroupId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(optionMapper.save(any())).thenThrow(DataIntegrityViolationException.class);
    Throwable throwable = Assertions.catchThrowableOfType(
        () -> optionService.registerOption(nameIsNull),
        DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 옵션수정_void(){
    // When
    when(optionMapper.update(any())).thenReturn(1);
    optionService.updateOption(any());

    // Then
    verify(optionMapper).update(any());
  }

  @Test
  void 옵션수정_badInputException_업데이트못한경우(){
    // When
    when(optionMapper.update(any())).thenReturn(0);
    Exception exception = catchException(() -> optionService.updateOption(any()));

    // Then
    assertThat(exception).isInstanceOf(BadInputException.class);
  }
}