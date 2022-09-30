package com.flab.commerce.domain.menu;

import static com.flab.commerce.util.Constants.LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MenuPatchDtoTest {

  Validator validator;

  @BeforeAll
  void setUp() {
    Locale.setDefault(Locale.US);
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 검증_void_전부Null() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_전부NotNull() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_name만Null() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .price(BigInteger.valueOf(10000L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_name만NotNull() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_가격만Null() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_가격만NotNull() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .price(BigInteger.valueOf(10000L))
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_이미지만Null() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10000L))
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_void_이미지만NotNull() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 검증_실패_이름길이2자미만() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈")
        .price(BigInteger.valueOf(10000L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(2, 20));
    assertThat(menuPatchDto.getName().length()).isLessThan(2);
  }

  @Test
  void 검증_실패_이름길이30자초과() {
    // Given
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, 31).forEach(sb::append);
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name(sb.toString())
        .price(BigInteger.valueOf(10000L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(2, 20));
  }

  @Test
  void 검증_실패_이름이빈값() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("")
        .price(BigInteger.valueOf(10000L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).hasSize(1);
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(2, 20));
  }

  @Test
  void 검증_실패_가격이음수() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(-1L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "must be greater than or equal to 0");
  }

  @Test
  void 검증_실패_가격이천만원초과() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(10_000_001L))
        .image(UUID.randomUUID() + ".png")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "must be less than or equal to 10000000");
  }

  @Test
  void 검증_실패_이미지40자미만() {
    // Given
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, 39).forEach(sb::append);
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(15_000L))
        .image(sb.toString())
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(40, 41));

  }

  @Test
  void 검증_실패_이미지41자초과() {
    // Given
    StringBuilder sb = new StringBuilder();
    IntStream.range(0, 42).forEach(sb::append);
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(15_000L))
        .image(sb.toString())
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(40, 41));

  }

  @Test
  void 검증_실패_이미지가빈값() {
    // Given
    MenuPatchDto menuPatchDto = MenuPatchDto.builder()
        .name("돈까스")
        .price(BigInteger.valueOf(15_000L))
        .image("")
        .build();

    // When
    Set<ConstraintViolation<MenuPatchDto>> validate = validator.validate(menuPatchDto);

    // Then
    assertThat(validate).hasSize(1);
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(40, 41));
  }
}