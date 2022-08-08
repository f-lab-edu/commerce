package com.flab.commerce.domain.user.dto;

import static com.flab.commerce.util.Constants.LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX;
import static com.flab.commerce.util.Constants.MUST_NOT_BE_BLANK;
import static org.assertj.core.api.Assertions.assertThat;

import com.flab.commerce.domain.user.LoginDto;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginDtoTest {

  Validator validator;

  @BeforeAll
  void setUp() {
    Locale.setDefault(Locale.US);
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 로그인입력데이터검증_성공() {
    // Given
    LoginDto loginDto = new LoginDto("email@email.com", "12345678", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations).isEmpty();
  }

  @Test
  void 로그인입력데이터검증_실패_이메일이_Null() {
    // Given
    LoginDto loginDto = new LoginDto(null, "12345678", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isOne();
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 로그인입력데이터검증_실패_이메일이_빈값() {
    // Given
    LoginDto loginDto = new LoginDto("", "12345678", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(1);
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 로그인입력데이터검증_실패_패스워드가_Null() {
    // Given
    LoginDto loginDto = new LoginDto("email@email.com", null, "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isOne();
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 로그인입력데이터검증_실패_패스워드가_빈값() {
    // Given
    LoginDto loginDto = new LoginDto("email@email.com", "", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(2);
    List<String> message = constraintViolations.stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(message).containsExactlyInAnyOrder(MUST_NOT_BE_BLANK,
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(8, 15)
    );
  }

  @Test
  void 로그인입력데이터검증_실패_이메일과_패스워드가_Null() {
    // Given
    LoginDto loginDto = new LoginDto(null, null, "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(2);
    List<String> message = constraintViolations.stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(message).contains(MUST_NOT_BE_BLANK);
  }

  @Test
  void 로그인입력데이터검증_실패_이메일_LOCAL_64_초과() {
    // Given
    String local = IntStream.range(0, 6).mapToObj(i -> "1234567890").collect(Collectors.joining());
    local += "123456";
    String email = local + "@email.com";
    LoginDto loginDto = new LoginDto(email, "12345678", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(1);
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(
        "must be a well-formed email address");
  }

  @Test
  void 로그인입력데이터검증_실패_이메일_Label_63_초과() {
    // Given
    String label = IntStream.range(0, 6).mapToObj(i -> "1234567890").collect(Collectors.joining());
    label += "1234";
    String email = "test@" + label + ".com";
    LoginDto loginDto = new LoginDto(email, "12345678", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(1);
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(
        "must be a well-formed email address");
  }

  @Test
  void 로그인입력데이터검증_실패_이메일_Domain_255_초과() {
    // Given
    String labelPart = IntStream
        .range(0, 6)
        .mapToObj(i -> "1234567890")
        .collect(Collectors.joining()) + "12";

    String label = IntStream
        .range(0, 4)
        .mapToObj(i -> labelPart + ".")
        .collect(Collectors.joining());

    String email = "test@" + label + "1234";
    LoginDto loginDto = new LoginDto(email, "12345678", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(1);
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(
        "must be a well-formed email address");
  }

  @Test
  void 로그인입력데이터검증_실패_패스워드가_길이_8_미만() {
    // Given
    LoginDto loginDto = new LoginDto("email@email.com", "1234567", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(1);
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(8, 15));
  }

  @Test
  void 로그인입력데이터검증_실패_패스워드가_길이_15_초과() {
    // Given
    LoginDto loginDto = new LoginDto("email@email.com", "1234567890123456", "/users/login");

    // When
    Collection<ConstraintViolation<LoginDto>> constraintViolations = validator.validate(
        loginDto);

    // Then
    assertThat(constraintViolations.size()).isEqualTo(1);
    assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(8, 15));
  }
}