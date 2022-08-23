package com.flab.commerce.domain.menuoptiongroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.commerce.exception.BadInputException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;

@ExtendWith(MockitoExtension.class)
class MenuOptionGroupServiceTest {

  @InjectMocks
  MenuOptionGroupService menuOptionGroupService;

  @Mock
  MenuOptionGroupMapper menuOptionGroupMapper;

  final long menuId = 1L;
  final long optionGroupId = 2L;
  final long optionGroupId2 = 3L;

  @Test
  void 삭제후저장_void() {
    // Given
    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menuId)
        .optionGroupId(optionGroupId)
        .build();

    MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
        .menuId(menuId)
        .optionGroupId(optionGroupId2)
        .build();

    List<MenuOptionGroup> menuOptionGroups = Arrays.asList(menuOptionGroup, menuOptionGroup2);

    // When
    menuOptionGroupService.saveAfterDeletion(menuId, menuOptionGroups);

    // Then
    verify(menuOptionGroupMapper).deleteByMenuId(menuId);
    verify(menuOptionGroupMapper).saveAll(menuOptionGroups);
  }

  @Test
  void 삭제후저장_duplicateKeyException_메뉴아이디와옵션그룹중복() {
    // Given
    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menuId)
        .optionGroupId(optionGroupId)
        .build();
    MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
        .menuId(menuId)
        .optionGroupId(optionGroupId)
        .build();
    List<MenuOptionGroup> menuOptionGroups = Arrays.asList(menuOptionGroup, menuOptionGroup2);

    // When
    doThrow(DuplicateKeyException.class).when(menuOptionGroupMapper).saveAll(menuOptionGroups);
    Exception exception = Assertions.catchException(
        () -> menuOptionGroupService.saveAfterDeletion(menuId, menuOptionGroups));

    // Then
    assertThat(exception).isInstanceOf(DuplicateKeyException.class);
    verify(menuOptionGroupMapper).deleteByMenuId(menuId);
    verify(menuOptionGroupMapper).saveAll(menuOptionGroups);
  }

  @Test
  void 삭제후저장_badSqlGrammarException_리스트빈값() {
    // When
    List<MenuOptionGroup> emptyList = Collections.emptyList();
    doThrow(BadSqlGrammarException.class).when(menuOptionGroupMapper).saveAll(emptyList);
    Exception exception = Assertions.catchException(
        () -> menuOptionGroupService.saveAfterDeletion(menuId, emptyList));

    // Then
    assertThat(exception).isInstanceOf(BadSqlGrammarException.class);
    verify(menuOptionGroupMapper).deleteByMenuId(menuId);
    verify(menuOptionGroupMapper).saveAll(emptyList);
  }

  @Test
  void 삭제후저장_myBatisSystemException_null() {
    // When
    doThrow(MyBatisSystemException.class).when(menuOptionGroupMapper).saveAll(null);
    Exception exception = Assertions.catchException(
        () -> menuOptionGroupService.saveAfterDeletion(menuId, null));

    // Then
    assertThat(exception).isInstanceOf(MyBatisSystemException.class);
    verify(menuOptionGroupMapper).deleteByMenuId(menuId);
    verify(menuOptionGroupMapper).saveAll(null);
  }

  @Test
  void 중복검증_void() {
    // When
    when(menuOptionGroupMapper.menuIdAndOptionGroupIdExists(menuId, optionGroupId)).thenReturn(
        false);
    menuOptionGroupService.validateDuplicate(menuId, optionGroupId);

    // Then
    verify(menuOptionGroupMapper).menuIdAndOptionGroupIdExists(menuId, optionGroupId);
  }

  @Test
  void 중복검증_badInputException_중복() {
    // When
    when(menuOptionGroupMapper.menuIdAndOptionGroupIdExists(menuId, optionGroupId)).thenReturn(
        true);
    Exception exception = catchException(
        () -> menuOptionGroupService.validateDuplicate(menuId, optionGroupId));

    // Then
    assertThat(exception).isInstanceOf(BadInputException.class);
    verify(menuOptionGroupMapper).menuIdAndOptionGroupIdExists(menuId, optionGroupId);
  }
}