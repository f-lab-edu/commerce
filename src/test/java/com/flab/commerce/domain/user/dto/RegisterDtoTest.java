package com.flab.commerce.domain.user.dto;

import static com.flab.commerce.util.Constants.LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX;
import static com.flab.commerce.util.Constants.MUST_NOT_BE_BLANK;
import static org.assertj.core.api.Assertions.assertThat;

import com.flab.commerce.domain.user.RegisterDto;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterDtoTest {

  Validator validator;


  @BeforeAll
  void setUp() {
    Locale.setDefault(Locale.US);
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 회원가입입력데이터검증_성공() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@gmail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate).isEmpty();
  }

  @Test
  void 회원가입입력데이터검증_실패_이메일이_Null() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email(null)
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 회원가입입력데이터검증_실패_이메일이_빈값() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("")
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 회원가입입력데이터검증_실패_패스워드가_Null() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password(null)
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 회원가입입력데이터검증_실패_패스워드가_빈값() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    List<String> messages = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(messages).contains(MUST_NOT_BE_BLANK, LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(8, 15));
  }

  @Test
  void 회원가입입력데이터검증_실패_패스워드가_8자_미만() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("1234567")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "length must be between 8 and 15");
  }

  @Test
  void 회원가입입력데이터검증_실패_패스워드가_15_초과() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("1234567890123456")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        "length must be between 8 and 15");
  }

  @Test
  void 회원가입입력데이터검증_실패_zipcode가_null() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode(null)
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 회원가입입력데이터검증_실패_zipcode가_빈값() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isEqualTo(2);
    List<String> messages = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(messages).contains(MUST_NOT_BE_BLANK, LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(3, 5));
  }

  @Test
  void 회원가입입력데이터검증_실패_zipcode_3자_미만() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(3, 5));
  }

  @Test
  void 회원가입입력데이터검증_실패_zipcode_5자_초과() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("123456")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(3, 5));
  }

  @Test
  void 회원가입입력데이터검증_실패_이름_2자_미만() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(2, 30));
  }


  @Test
  void 회원가입입력데이터검증_실패_이름_30자_초과() {
    // Given
    String stringLength30 = IntStream.range(0, 3).mapToObj(i -> "1234567890")
        .collect(Collectors.joining());
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name(stringLength30 + "1")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(2, 30));
  }

  @Test
  void 회원가입입력데이터검증_실패_이름이_null() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name(null)
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 회원가입입력데이터검증_실패_이름이_빈값() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("")
        .zipcode("12345")
        .address("서울특별시 강남구 강남대로98길 20, 5층 플라타너스(역삼동)")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isEqualTo(2);
    List<String> messages = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(messages).contains(MUST_NOT_BE_BLANK, LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(2, 30));
  }

  @Test
  void 회원가입입력데이터검증_실패_주소_10자_미만() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("123456789")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(10, 40));
  }

  @Test
  void 회원가입입력데이터검증_실패_주소_40자_초과() {
    // Given
    String stringLength40 = IntStream.range(0, 4).mapToObj(i -> "1234567890")
        .collect(Collectors.joining());
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address(stringLength40 + "1")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(
        LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(10, 40));
  }

  @Test
  void 회원가입입력데이터검증_실패_주소가_null() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address(null)
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isOne();
    assertThat(validate.iterator().next().getMessage()).isEqualTo(MUST_NOT_BE_BLANK);
  }

  @Test
  void 회원가입입력데이터검증_실패_주소가_빈값() {
    // Given
    RegisterDto registerDto = RegisterDto.builder()
        .email("test@mail.com")
        .name("홍길동")
        .zipcode("12345")
        .address("")
        .phone("010-1234-5678")
        .password("12345678")
        .build();

    // When
    Set<ConstraintViolation<RegisterDto>> validate = validator.validate(registerDto);

    // Then
    assertThat(validate.size()).isEqualTo(2);
    List<String> messages = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    assertThat(messages).contains(MUST_NOT_BE_BLANK, LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(10, 40));
  }
}