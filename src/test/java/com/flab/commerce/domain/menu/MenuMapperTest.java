package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.domain.store.Store;
import com.flab.commerce.domain.store.StoreMapper;
import com.flab.commerce.domain.store.StoreStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
class MenuMapperTest {

  @Autowired
  MenuMapper menuMapper;

  @Autowired
  StoreMapper storeMapper;

  @Autowired
  OwnerMapper ownerMapper;

  @Test
  void 메뉴등록_성공() {
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

    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    int insertCount = menuMapper.register(menu);

    // Then
    assertThat(insertCount).isOne();
    Menu foundMenu = menuMapper.findById(menu.getId());
    assertThat(foundMenu.getId()).isEqualTo(menu.getId());
    assertThat(foundMenu.getStoreId()).isEqualTo(menu.getStoreId());
  }

  @Test
  void 메뉴등록_DataIntegrityViolationException_존재하지않는가게ID() {
    // Given
    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(-1L)
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    // When
    Throwable throwable = catchThrowableOfType(
        () -> menuMapper.register(menu), DataIntegrityViolationException.class);

    // Then
    assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 가게아이디로조회() {
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

    Menu menu1 = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    Menu menu2 = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    menuMapper.register(menu1);
    menuMapper.register(menu2);

    // When
    List<Menu> menus = menuMapper.findByStoreId(store.getId());

    // Then
    List<Long> ids = menus.stream().map(Menu::getId).collect(Collectors.toList());
    assertThat(menus).hasSize(2);
    assertThat(ids).contains(menu1.getId(), menu2.getId());
  }

  @Test
  void 가게아이디로조회_empty_가게에메뉴가없는경우() {
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

    // When
    List<Menu> menus = menuMapper.findByStoreId(store.getId());

    // Then
    assertThat(menus).isEmpty();
  }

  @Test
  void 가게아이디로조회_empty_가게아이디가null() {
    // When
    List<Menu> menus = menuMapper.findByStoreId(null);

    // Then
    assertThat(menus).isEmpty();
  }

  @Test
  void 메뉴삭제_조건만족() {
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

    Menu menu1 = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    Menu menu2 = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    menuMapper.register(menu1);
    menuMapper.register(menu2);

    // When
    menuMapper.deleteByIdAndStoreId(menu1.getId(), store.getId());

    // Then
    List<Menu> menus = menuMapper.findByStoreId(store.getId());
    Menu menu = menuMapper.findById(menu1.getId());

    assertThat(menus).hasSize(1);
    assertThat(menu).isNull();
  }

  @Test
  void 메뉴삭제_조건불만족_다른가게의메뉴() {
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

    Store store1 = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store1);

    Store store2 = Store.builder()
        .name("홍콩반점")
        .address("서울시 서초구 반포동")
        .phone("021231234")
        .description("중국집")
        .status(StoreStatus.OPEN)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .ownerId(owner.getId())
        .build();
    storeMapper.register(store2);

    Menu menu1 = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store1.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    Menu menu2 = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store2.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    menuMapper.register(menu1);
    menuMapper.register(menu2);

    // When
    menuMapper.deleteByIdAndStoreId(menu1.getId(), store2.getId());

    // Then
    List<Menu> store1Menus = menuMapper.findByStoreId(store1.getId());
    List<Menu> store2Menus = menuMapper.findByStoreId(store2.getId());
    Menu menu = menuMapper.findById(menu1.getId());

    assertThat(store1Menus).hasSize(1);
    assertThat(store2Menus).hasSize(1);
    assertThat(menu).isNotNull();
    assertThat(menu.getId()).isEqualTo(menu1.getId());
  }

  @Test
  void 메뉴삭제_조건불만족_없는메뉴() {
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
    Menu menu = Menu.builder()
        .name("돈까스")
        .price(BigDecimal.valueOf(10000L))
        .image("image")
        .storeId(store.getId())
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    menuMapper.register(menu);

    // When
    menuMapper.deleteByIdAndStoreId(-1L, store.getId());

    // Then;
    List<Menu> menus = menuMapper.findByStoreId(store.getId());
    Menu findMenu = menuMapper.findById(-1L);

    assertThat(menus).hasSize(1);
    assertThat(findMenu).isNull();
  }
}