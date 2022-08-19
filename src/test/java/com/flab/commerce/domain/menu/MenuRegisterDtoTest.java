package com.flab.commerce.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MenuRegisterDtoTest {

  Validator validator;

  @BeforeAll
  void setUp() {
    Locale.setDefault(Locale.US);
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 검증_성공() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(10000L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_실패_이름길이2자미만() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈")
        .price(10000L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "length must be between 2 and 20");
    assertThat(menuRegisterDto.getName().length()).isLessThan(2);
  }

  @Test
  void 검증_실패_이름길이30자초과() {
    // Given
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, 31).forEach(sb::append);
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name(sb.toString())
        .price(10000L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "length must be between 2 and 20");
  }

  @Test
  void 검증_실패_이름이Null() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name(null)
        .price(10000L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo("must not be blank");
  }

  @Test
  void 검증_실패_이름이빈값() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("")
        .price(10000L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate).hasSize(2);
    List<String> message = validate.stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(message).contains("must not be blank",
        "length must be between 2 and 20"
    );
  }

  @Test
  void 검증_실패_가격이음수() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(-1L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "must be greater than or equal to 0");
  }

  @Test
  void 검증_실패_가격이천만원초과() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(10_000_001L)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "must be less than or equal to 10000000");
  }

  @Test
  void 검증_실패_가격이null() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(null)
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo("must not be null");
  }

  @Test
  void 검증_실패_이미지40자미만() {
    // Given
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, 39).forEach(sb::append);
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(15_000L)
        .image(sb.toString())
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo("length must be between 40 and 41");
  }

  @Test
  void 검증_실패_이미지41자초과() {
    // Given
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, 42).forEach(sb::append);
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(15_000L)
        .image(sb.toString())
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo("length must be between 40 and 41");
  }

  @Test
  void 검증_실패_이미지가null() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(15_000L)
        .image(null)
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo("must not be blank");
  }

  @Test
  void 검증_실패_이미지가빈값() {
    // Given
    MenuRegisterDto menuRegisterDto = MenuRegisterDto.builder()
        .name("돈까스")
        .price(15_000L)
        .image("")
        .build();

    // When
    Set<ConstraintViolation<MenuRegisterDto>> validate = validator.validate(menuRegisterDto);
    List<String> message = validate.stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());

    // Then
    assertThat(validate).hasSize(2);
    assertThat(message).contains("must not be blank",
        "length must be between 40 and 41"
    );
  }
}