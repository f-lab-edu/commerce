package com.flab.commerce.domain.optiongroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flab.commerce.domain.option.Option;
import com.flab.commerce.domain.option.OptionMapper;
import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.Store;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.store.StoreStatus;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

@MybatisTest
class OptionGroupMapperTest {

  @Autowired
  OwnerMapper ownerMapper;

  @Autowired
  StoreMapper storeMapper;

  @Autowired
  OptionGroupMapper optionGroupMapper;

  @Autowired
  OptionMapper optionMapper;

  @Test
  void 옵션그룹저장_1(){
    // given
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int result = optionGroupMapper.save(optionGroup);

    // Then
    assertEquals(1, result);
  }

  @Test
  void 옵션그룹저장_DataIntegrityViolationException_컬럼NotNull(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name(null)
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    Throwable throwable = Assertions.catchThrowableOfType(
        () -> optionGroupMapper.save(optionGroup), DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }
  
  @Test
  void 가게id로옵션그룹찾기_OptionGroups(){
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

    Store store1 = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store1);

    Store store2 = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store2);

    OptionGroup optionGroup1 = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store1.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup1);
    
    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store1.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    OptionGroup optionGroup3 = OptionGroup.builder()
        .name("옵션그룹3")
        .storeId(store2.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup3);

    // When
    List<OptionGroup> optionGroups = optionGroupMapper.findByStoreId(store1.getId());
    List<Long> ids = optionGroups.stream().map(OptionGroup::getId).collect(Collectors.toList());

    // Then
    assertThat(optionGroups).hasSize(2);
    assertThat(ids).contains(optionGroup1.getId(), optionGroup2.getId());
  }

  @Test
  void 가게id로옵션그룹찾기_empty_가게의옵셥그룹이없는경우(){
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

    // When
    List<OptionGroup> optionGroups = optionGroupMapper.findByStoreId(store.getId());

    // Then
    assertThat(optionGroups).isEmpty();
  }

  @Test
  void 삭제_1건(){
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

    OptionGroup optionGroup1 = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup1);

    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    // When
    int countDeleted = optionGroupMapper.delete(optionGroup1.getId());
    List<OptionGroup> optionGroups = optionGroupMapper.findByStoreId(store.getId());

    // Then
    assertThat(countDeleted).isOne();
    assertThat(optionGroups).hasSize(1);
    assertThat(optionGroups.get(0).getId()).isEqualTo(optionGroup2.getId());
  }

  @Test
  void 삭제_0건_존재하지않는메뉴삭제(){
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

    OptionGroup optionGroup1 = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup1);

    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    // When
    int countDeleted = optionGroupMapper.delete(-1L);
    List<OptionGroup> optionGroups = optionGroupMapper.findByStoreId(store.getId());
    List<Long> ids = optionGroups.stream().map(OptionGroup::getId).collect(Collectors.toList());

    // Then
    assertThat(countDeleted).isZero();
    assertThat(optionGroups).hasSize(2);
    assertThat(ids).contains(optionGroup1.getId(), optionGroup2.getId());
  }

  @Test
  void 가게id가존재한다_true(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    // When
    boolean exists = optionGroupMapper.idExists(optionGroup.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void 가게id가존재한다_false(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);
    optionGroupMapper.delete(optionGroup.getId());

    // When
    boolean exists = optionGroupMapper.idExists(store.getId());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void id와가게id가존재한다_true(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    // When
    boolean exists = optionGroupMapper.idAndStoreIdExists(optionGroup.getId(), store.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void id와가게id가존재한다_false_다른가게의메뉴(){
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

    Store store1 = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store1);

    Store store2 = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store2);

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store1.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    // When
    boolean exists = optionGroupMapper.idAndStoreIdExists(optionGroup.getId(), store2.getId());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void 업데이트_1(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    OptionGroup update = OptionGroup.builder()
        .id(optionGroup.getId())
        .name("업데이트 옵션그룹")
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int countUpdated = optionGroupMapper.update(update);
    OptionGroup updated = optionGroupMapper.findById(optionGroup.getId());

    // Then
    assertThat(countUpdated).isOne();
    assertThat(updated.getName()).isEqualTo(update.getName());
    assertThat(updated.getName()).isNotEqualTo(optionGroup.getName());
  }

  @Test
  void 업데이트_0_옵션그룹이존재하지않는겨우(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    OptionGroup update = OptionGroup.builder()
        .id(-1L)
        .name("업데이트 옵션그룹")
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int countUpdated = optionGroupMapper.update(update);
    OptionGroup updated = optionGroupMapper.findById(optionGroup.getId());

    // Then
    assertThat(countUpdated).isZero();
    assertThat(updated.getName()).isNotEqualTo(update.getName());
    assertThat(updated.getName()).isEqualTo(optionGroup.getName());
  }

  @Test
  void 옵션그룹과옵션들조회_optionGroup_옵션그룹과옵션들(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    Option option1 = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option1);

    Option option2 = Option.builder()
        .name("옵션2")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option2);

    // When
    OptionGroup selected = optionGroupMapper.selectOptionGroupAndOptions(optionGroup.getId());
    List<String> names = selected.getOptions().stream().map(Option::getName)
        .collect(Collectors.toList());
    List<BigInteger> prices = selected.getOptions().stream().map(Option::getPrice)
        .collect(Collectors.toList());

    // Then
    assertThat(selected).isNotNull();
    assertThat(selected.getName()).isEqualTo(optionGroup.getName());
    assertThat(selected.getStoreId()).isEqualTo(optionGroup.getStoreId());
    assertThat(selected.getOptions()).hasSize(2);
    assertThat(names).contains(option1.getName(), option2.getName());
    assertThat(prices).contains(option1.getPrice(), option2.getPrice());
  }

  @Test
  void 옵션그룹과옵션들조회_optionGroup_옵션이없는경우(){
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    // When
    OptionGroup selected = optionGroupMapper.selectOptionGroupAndOptions(optionGroup.getId());

    // Then
    assertThat(selected).isNotNull();
    assertThat(selected.getName()).isEqualTo(optionGroup.getName());
    assertThat(selected.getStoreId()).isEqualTo(optionGroup.getStoreId());
    assertThat(selected.getOptions()).isEmpty();
  }

  @Test
  void 옵션그룹과옵션들조회_null_옵션그룹이존재하지않는경우(){
    // When
    OptionGroup selected = optionGroupMapper.selectOptionGroupAndOptions(1L);

    // Then
    assertThat(selected).isNull();
  }

  @Test
  void 아이디로찾기In_2() {
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

    OptionGroup optionGroup = OptionGroup.builder()
        .name("옵션그룹1")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup);

    OptionGroup optionGroup2 = OptionGroup.builder()
        .name("옵션그룹2")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionGroupMapper.save(optionGroup2);

    // When
    List<OptionGroup> optionGroups = optionGroupMapper.findByIdIn(
        new HashSet<>(Arrays.asList(optionGroup.getId(), optionGroup2.getId())));
    List<Long> ids = optionGroups.stream().map(OptionGroup::getId).collect(Collectors.toList());

    // Then
    assertThat(optionGroups).hasSize(2);
    assertThat(ids).contains(optionGroup.getId(), optionGroup2.getId());
  }
}