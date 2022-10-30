package com.flab.commerce.domain.option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.flab.commerce.domain.optiongroup.OptionGroup;
import com.flab.commerce.domain.optiongroup.OptionGroupMapper;
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
class OptionMapperTest {

  @Autowired
  OwnerMapper ownerMapper;

  @Autowired
  StoreMapper storeMapper;

  @Autowired
  OptionGroupMapper optionGroupMapper;

  @Autowired
  OptionMapper optionMapper;

  @Test
  void 옵션저장_1() {
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    // When
    int countSaved = optionMapper.save(option);
    Option savedOption = optionMapper.findById(option.getId());

    // Then
    assertThat(countSaved).isOne();
    assertThat(savedOption.getName()).isEqualTo(option.getName());
    assertThat(savedOption.getPrice()).isEqualTo(option.getPrice());
  }

  @Test
  void 옵션저장_dataIntegrityViolationException_이름이null() {
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

    Option option = Option.builder()
        .name(null)
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    // When
    Exception exception = catchException(() -> optionMapper.save(option));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 옵션저장_dataIntegrityViolationException_가격이null() {
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

    Option option = Option.builder()
        .name("옵션1")
        .price(null)
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    // When
    Exception exception = catchException(() -> optionMapper.save(option));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 옵션저장_dataIntegrityViolationException_옵션그룹이존재하지않는경우() {
    // Given
    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(-1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    Exception exception = catchException(() -> optionMapper.save(option));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 옵션업데이트_1(){
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);

    Option updateOption = Option.builder()
        .id(option.getId())
        .name("옵션2")
        .price(BigInteger.valueOf(1001))
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int update = optionMapper.update(updateOption);
    Option updatedOption = optionMapper.findById(option.getId());

    // Then
    assertThat(update).isOne();
    assertThat(updatedOption.getName()).isEqualTo(updateOption.getName());
    assertThat(updatedOption.getPrice()).isEqualTo(updateOption.getPrice());

    assertThat(updatedOption.getName()).isNotEqualTo(option.getName());
    assertThat(updatedOption.getPrice()).isNotEqualTo(option.getPrice());
  }

  @Test
  void 옵션업데이트_0_옵션이존재하지않는경우(){
    Option updateOption = Option.builder()
        .id(-1L)
        .name("옵션2")
        .price(BigInteger.valueOf(1001))
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int update = optionMapper.update(updateOption);

    // Then
    assertThat(update).isZero();
  }

  @Test
  void 옵션업데이트_dataIntegrityViolationException_컬럼NotNull예외(){
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);

    Option updateOption = Option.builder()
        .id(option.getId())
        .name(null)
        .price(BigInteger.valueOf(1001))
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    Exception exception = catchException(() -> optionMapper.update(updateOption));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 옵션삭제_1(){
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);

    // When
    int countDeleted = optionMapper.delete(option.getId());
    boolean exists = optionMapper.idExists(option.getId());

    assertThat(countDeleted).isOne();
    assertThat(exists).isFalse();
  }

  @Test
  void 옵션삭제_0_옵션이존재하지않는경우(){
    // When
    int countDeleted = optionMapper.delete(-1L);
    boolean exists = optionMapper.idExists(-1L);

    assertThat(countDeleted).isZero();
    assertThat(exists).isFalse();
  }

  @Test
  void findById_option(){
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);
    // When
    Option foundOption = optionMapper.findById(option.getId());

    // Then
    assertThat(foundOption.getName()).isEqualTo(option.getName());
    assertThat(foundOption.getPrice()).isEqualTo(option.getPrice());
    assertThat(foundOption.getOptionGroupId()).isEqualTo(option.getOptionGroupId());
  }

  @Test
  void findById_null_옵션이존재하지않는경우(){
    // When
    Option foundOption = optionMapper.findById(-1L);

    // Then
    assertThat(foundOption).isNull();
  }

  @Test
  void 아이디가존재한다_ture(){
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);
    // When
    boolean exists = optionMapper.idExists(option.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void 아이디가존재한다_false(){
    // When
    boolean exists = optionMapper.idExists(-1L);

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void 아이디와옵션그룹아이디가존재한다_ture(){
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);
    // When
    boolean exists = optionMapper.idAndOptionGroupIdExists(option.getId(), optionGroup.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void 아이디와옵션그룹아이디가존재한다_false(){
    // When
    boolean exists = optionMapper.idAndOptionGroupIdExists(-1L, -1L);

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void 아이디로찾기In_2() {
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

    Option option = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option);

    Option option2 = Option.builder()
        .name("옵션1")
        .price(BigInteger.valueOf(1000))
        .optionGroupId(optionGroup.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    optionMapper.save(option2);

    // When
    List<Option> options = optionMapper.findByIdIn(new HashSet<>(Arrays.asList(option.getId(), option2.getId())));
    List<Long> ids = options.stream().map(Option::getId).collect(Collectors.toList());

    // Then
    assertThat(options).hasSize(2);
    assertThat(ids).contains(option.getId(), option2.getId());
  }
}