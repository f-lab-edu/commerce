package com.flab.commerce.domain.option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.flab.commerce.domain.optiongroup.OptionGroup;
import com.flab.commerce.domain.optiongroup.OptionGroupMapper;
import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.Store;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.store.StoreStatus;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
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
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
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
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
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
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();
    ownerMapper.register(owner);

    Store store = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
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
}