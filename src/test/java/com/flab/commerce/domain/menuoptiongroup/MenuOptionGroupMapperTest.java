package com.flab.commerce.domain.menuoptiongroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuMapper;
import com.flab.commerce.domain.optiongroup.OptionGroup;
import com.flab.commerce.domain.optiongroup.OptionGroupMapper;
import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.Store;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.store.StoreStatus;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;

@MybatisTest
class MenuOptionGroupMapperTest {

  @Autowired
  MenuOptionGroupMapper menuOptionGroupMapper;

  @Autowired
  OwnerMapper ownerMapper;

  @Autowired
  StoreMapper storeMapper;

  @Autowired
  MenuMapper menuMapper;

  @Autowired
  OptionGroupMapper optionGroupMapper;

  @Test
  void 모두저장_2() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup2.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int countSaved = menuOptionGroupMapper.saveAll(
        Arrays.asList(menuOptionGroup, menuOptionGroup2));
    MenuOptionGroup menuOptionGroupFound = menuOptionGroupMapper.findById(menuOptionGroup.getId());
    MenuOptionGroup menuOptionGroupFound2 = menuOptionGroupMapper.findById(
        menuOptionGroup2.getId());

    // Then
    assertThat(countSaved).isEqualTo(2);

    assertThat(menuOptionGroupFound).isNotNull();
    assertThat(menuOptionGroupFound.getMenuId()).isEqualTo(menuOptionGroup.getMenuId());
    assertThat(menuOptionGroupFound.getOptionGroupId()).isEqualTo(
        menuOptionGroup.getOptionGroupId());

    assertThat(menuOptionGroupFound2).isNotNull();
    assertThat(menuOptionGroupFound2.getMenuId()).isEqualTo(menuOptionGroup2.getMenuId());
    assertThat(menuOptionGroupFound2.getOptionGroupId()).isEqualTo(
        menuOptionGroup2.getOptionGroupId());
  }

  @Test
  void 모두저장_duplicateKeyException_메뉴아이디와옵션그룹중복() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuOptionGroupMapper.saveAll(Collections.singletonList(menuOptionGroup));
    // When
    Exception exception = Assertions.catchException(
        () -> menuOptionGroupMapper.saveAll(Collections.singletonList(menuOptionGroup)));

    // Then
    assertThat(exception).isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void 모두저장_badSqlGrammarException_리스트빈값() {
    // When
    Exception exception = catchException(
        () -> menuOptionGroupMapper.saveAll(Collections.emptyList()));
    // Then
    assertThat(exception).isInstanceOf(BadSqlGrammarException.class);
  }

  @Test
  void 모두저장_myBatisSystemException_null() {
    // When
    Exception exception = catchException(
        () -> menuOptionGroupMapper.saveAll(null));
    // Then
    assertThat(exception).isInstanceOf(MyBatisSystemException.class);
  }

  @Test
  void 아이디로찾기_MenuOptionGroup() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuOptionGroupMapper.saveAll(Collections.singletonList(menuOptionGroup));

    // When
    MenuOptionGroup menuOptionGroupFound = menuOptionGroupMapper.findById(menuOptionGroup.getId());

    // Then
    assertThat(menuOptionGroupFound).isNotNull();
    assertThat(menuOptionGroupFound.getMenuId()).isEqualTo(menuOptionGroup.getMenuId());
    assertThat(menuOptionGroupFound.getOptionGroupId()).isEqualTo(
        menuOptionGroup.getOptionGroupId());
  }

  @Test
  void 아이디로찾기_null_메뉴옵션그룹이존재하지않는경우() {
    // When
    MenuOptionGroup menuOptionGroupFound = menuOptionGroupMapper.findById(1L);

    // Then
    assertThat(menuOptionGroupFound).isNull();
  }

  @Test
  void 메뉴아이디와옵션그룹아이디가존재합니다_true() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuOptionGroupMapper.saveAll(Collections.singletonList(menuOptionGroup));

    // When
    boolean exist = menuOptionGroupMapper.menuIdAndOptionGroupIdExists(menu.getId(),
        optionGroup.getId());

    // Then
    assertThat(exist).isTrue();
  }

  @Test
  void 메뉴아이디와옵션그룹아이디가존재합니다_false() {
    // When
    boolean exist = menuOptionGroupMapper.menuIdAndOptionGroupIdExists(1L, 2L);

    // Then
    assertThat(exist).isFalse();
  }

  @Test
  void 메뉴아이디로삭제() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    Menu menu2 = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu2);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    OptionGroup optionGroup3 = OptionGroup.builder()
        .name("옵션")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup3);

    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup2.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    MenuOptionGroup menuOptionGroup3 = MenuOptionGroup.builder()
        .menuId(menu2.getId())
        .optionGroupId(optionGroup2.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    MenuOptionGroup menuOptionGroup4 = MenuOptionGroup.builder()
        .menuId(menu2.getId())
        .optionGroupId(optionGroup3.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    menuOptionGroupMapper.saveAll(Arrays.asList(menuOptionGroup, menuOptionGroup2));
    menuOptionGroupMapper.saveAll(Arrays.asList(menuOptionGroup3, menuOptionGroup4));

    // When
    int countDeleted = menuOptionGroupMapper.deleteByMenuId(menu.getId());
    List<MenuOptionGroup> menuOptionGroups = menuOptionGroupMapper.findByMenuId(menu.getId());
    List<MenuOptionGroup> menuOptionGroups2 = menuOptionGroupMapper.findByMenuId(menu2.getId());

    // Then
    assertThat(countDeleted).isEqualTo(2);
    assertThat(menuOptionGroups).isEmpty();
    assertThat(menuOptionGroups2).hasSize(2);
  }

  @Test
  void 메뉴아이디로삭제_0_메뉴옵션그룹이없는경우() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    // When
    int countDeleted = menuOptionGroupMapper.deleteByMenuId(menu.getId());
    List<MenuOptionGroup> menuOptionGroups = menuOptionGroupMapper.findByMenuId(menu.getId());

    // Then
    assertThat(countDeleted).isZero();
    assertThat(menuOptionGroups).isEmpty();
  }

  @Test
  void 상점아이디로찾기_menuOptionGroups() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스1")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    Menu menu2 = Menu.builder()
        .name("돈까스2")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu2);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션2")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
        .menuId(menu2.getId())
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    MenuOptionGroup menuOptionGroup3 = MenuOptionGroup.builder()
        .menuId(menu.getId())
        .optionGroupId(optionGroup2.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuOptionGroupMapper.saveAll(
        Arrays.asList(menuOptionGroup, menuOptionGroup2, menuOptionGroup3));

    // When
    List<MenuOptionGroup> menuOptionGroups = menuOptionGroupMapper.findByStoreId(store.getId());
    MenuOptionGroup selectMenuOptionGroup = menuOptionGroups.get(0);
    List<String> menuNames = selectMenuOptionGroup.getMenus().stream().map(Menu::getName)
        .collect(Collectors.toList());

    MenuOptionGroup selectMenuOptionGroup2 = menuOptionGroups.get(1);
    List<String> menuNames2 = selectMenuOptionGroup2.getMenus().stream().map(Menu::getName)
        .collect(Collectors.toList());

    // then
    assertThat(menuOptionGroups).isNotNull().hasSize(2);
    assertThat(selectMenuOptionGroup.getMenus()).hasSize(2);
    assertThat(selectMenuOptionGroup.getOptionGroup().getName()).isEqualTo(optionGroup.getName());
    assertThat(menuNames).contains(menu.getName(), menu2.getName());

    assertThat(selectMenuOptionGroup2.getMenus()).hasSize(1);
    assertThat(selectMenuOptionGroup2.getOptionGroup().getName()).isEqualTo(optionGroup2.getName());
    assertThat(menuNames2).contains(menu.getName());
  }

  @Test
  void 상점아이디로찾기_menuOptionGroups_메뉴가없는경우() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스1")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    // When
    List<MenuOptionGroup> menuOptionGroups = menuOptionGroupMapper.findByStoreId(store.getId());
    MenuOptionGroup selectMenuOptionGroup = menuOptionGroups.get(0);

    // then
    assertThat(menuOptionGroups).isNotNull().hasSize(1);
    assertThat(selectMenuOptionGroup.getMenus()).isEmpty();
    assertThat(selectMenuOptionGroup.getOptionGroup().getName()).isEqualTo(optionGroup.getName());
  }

  @Test
  void 상점아이디로찾기_빈값_옵션그룹이없는경우() {
    // Given
    Owner owner = Owner.builder()
        .email("bgpark82@gmail.com")
        .password("1234")
        .name("박병길")
        .phone("0101231234")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store);

    Menu menu = Menu.builder()
        .name("돈까스1")
        .price(BigInteger.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    // When
    List<MenuOptionGroup> menuOptionGroups = menuOptionGroupMapper.findByStoreId(store.getId());

    // then
    assertThat(menuOptionGroups).isEmpty();
  }
}