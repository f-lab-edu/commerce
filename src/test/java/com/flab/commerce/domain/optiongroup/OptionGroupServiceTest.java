package com.flab.commerce.domain.optiongroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.commerce.domain.option.Option;
import com.flab.commerce.exception.BadInputException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;

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

  @Test
  void getOptionGroups_OptionGroups() {
    // Given
    List<OptionGroup> optionGroups = Arrays.asList(OptionGroup.builder()
            .id(1L)
            .name("옵션1")
            .storeId(1L)
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build(),
        OptionGroup.builder()
            .id(2L)
            .name("옵션2")
            .storeId(1L)
            .createDateTime(ZonedDateTime.now())
            .modifyDateTime(ZonedDateTime.now())
            .build());
    // When
    when(optionGroupMapper.findByStoreId(1L)).thenReturn(optionGroups);
    List<OptionGroup> optionGroupsFound = optionGroupService.getOptionGroups(1L);

    // Then
    verify(optionGroupMapper).findByStoreId(1L);
    assertThat(optionGroupsFound).hasSize(optionGroups.size());
  }

  @Test
  void getOptionGroups_empty_가게의그룹메뉴가없는경우() {
    // Given
    List<OptionGroup> emptyList = Collections.emptyList();

    // When
    when(optionGroupMapper.findByStoreId(1L)).thenReturn(emptyList);
    List<OptionGroup> optionGroupsFound = optionGroupService.getOptionGroups(1L);

    // Then
    verify(optionGroupMapper).findByStoreId(1L);
    assertThat(optionGroupsFound).isEmpty();
  }

  @Test
  void 옵션그룹삭제_void() {
    // When
    when(optionGroupMapper.delete(1L)).thenReturn(1);
    optionGroupService.deleteOptionGroup(1L);

    // Then
    verify(optionGroupMapper).delete(1L);
  }

  @Test
  void 옵션그룹삭제_badInputException_삭제대상이없는경우() {
    // When
    when(optionGroupMapper.delete(1L)).thenReturn(0);
    Throwable throwable = Assertions.catchThrowable(
        () -> optionGroupService.deleteOptionGroup(1L));

    // Then
    assertThat(throwable).isInstanceOf(BadInputException.class);
    verify(optionGroupMapper).delete(1L);
  }

  @Test
  void 옵션그룹검증_void() {
    // When
    when(optionGroupMapper.idExists(any())).thenReturn(true);
    when(optionGroupMapper.idAndStoreIdExists(any(), any())).thenReturn(true);
    optionGroupService.validateOptionGroupStore(1L, 1L);

    // Then
    verify(optionGroupMapper).idExists(1L);
    verify(optionGroupMapper).idAndStoreIdExists(1L, 1L);
  }

  @Test
  void 옵션그룹검증_badInputException_옵션그룹이존재하지않는경우() {
    // When
    when(optionGroupMapper.idExists(any())).thenReturn(false);
    Throwable throwable = Assertions.catchThrowable(
        () -> optionGroupService.validateOptionGroupStore(1L, 1L));

    // Then
    assertThat(throwable).isInstanceOf(BadInputException.class);
    verify(optionGroupMapper).idExists(1L);
    verify(optionGroupMapper, never()).idAndStoreIdExists(1L, 1L);
  }

  @Test
  void 옵션그룹검증_accessDeniedException_다른가게의옵션그룹일경우() {
    // When
    when(optionGroupMapper.idExists(any())).thenReturn(true);
    when(optionGroupMapper.idAndStoreIdExists(any(), any())).thenReturn(false);
    Throwable throwable = Assertions.catchThrowable(
        () -> optionGroupService.validateOptionGroupStore(1L, 1L));

    // Then
    assertThat(throwable).isInstanceOf(AccessDeniedException.class);
    verify(optionGroupMapper).idExists(1L);
    verify(optionGroupMapper).idAndStoreIdExists(1L, 1L);
  }

  @Test
  void 옵션그룹수정_void() {
    // When
    when(optionGroupMapper.update(any())).thenReturn(1);
    optionGroupService.updateOptionGroup(any());

    // Then
    verify(optionGroupMapper).update(any());
  }

  @Test
  void 옵션그룹수정_badInputException_옵션그룹을찾을수없는경우() {
    // When
    when(optionGroupMapper.update(any())).thenReturn(0);
    Exception exception = catchException(() -> optionGroupService.updateOptionGroup(any()));

    // Then
    assertThat(exception).isInstanceOf(BadInputException.class);
    verify(optionGroupMapper).update(any());
  }

  @Test
  void 옵션그롭과옵션들을가져오기_optionGroupWithOptions_optionGroup() {
    // Given
    Option option1 = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    Option option2 = Option.builder()
        .name("옵션2")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    OptionGroup optionGroup = OptionGroup.builder()
        .id(1L)
        .name("옵션1")
        .storeId(1L)
        .options(Arrays.asList(option1, option2))
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(optionGroupMapper.selectOptionGroupAndOptions(optionGroup.getId())).thenReturn(
        optionGroup);
    OptionGroup optionGroupAndOptions = optionGroupService.getOptionGroupAndOptions(
        optionGroup.getId());
    List<String> names = optionGroupAndOptions.getOptions().stream().map(Option::getName)
        .collect(Collectors.toList());
    List<BigInteger> prices = optionGroupAndOptions.getOptions().stream().map(Option::getPrice)
        .collect(Collectors.toList());

    // Then
    verify(optionGroupMapper).selectOptionGroupAndOptions(optionGroup.getId());
    assertThat(optionGroupAndOptions).isNotNull();
    assertThat(optionGroupAndOptions.getName()).isEqualTo(optionGroup.getName());
    assertThat(optionGroupAndOptions.getStoreId()).isEqualTo(optionGroup.getStoreId());
    assertThat(optionGroupAndOptions.getOptions()).hasSize(2);
    assertThat(names).containsExactlyInAnyOrder(option1.getName(), option2.getName());
    assertThat(prices).containsExactlyInAnyOrder(option1.getPrice(), option2.getPrice());
  }

  @Test
  void 옵션그롭과옵션들을가져오기_optionGroup_옵션이없는경우() {
    // Given
    OptionGroup optionGroup = OptionGroup.builder()
        .id(1L)
        .name("옵션1")
        .storeId(1L)
        .options(Collections.emptyList())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    when(optionGroupMapper.selectOptionGroupAndOptions(optionGroup.getId())).thenReturn(
        optionGroup);
    OptionGroup optionGroupAndOptions = optionGroupService.getOptionGroupAndOptions(
        optionGroup.getId());

    // Then
    verify(optionGroupMapper).selectOptionGroupAndOptions(optionGroup.getId());

    assertThat(optionGroupAndOptions).isNotNull();
    assertThat(optionGroupAndOptions.getName()).isEqualTo(optionGroup.getName());
    assertThat(optionGroupAndOptions.getStoreId()).isEqualTo(optionGroup.getStoreId());
    assertThat(optionGroupAndOptions.getOptions()).isEmpty();}

  @Test
  void 옵션그롭과옵션들을가져오기_null_옵션그룹이없는경우() {
    // When
    when(optionGroupMapper.selectOptionGroupAndOptions(any())).thenReturn(null);
    OptionGroup optionGroupAndOptions = optionGroupService.getOptionGroupAndOptions(any());

    // Then
    verify(optionGroupMapper).selectOptionGroupAndOptions(any());

    assertThat(optionGroupAndOptions).isNull();
  }
}