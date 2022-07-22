package com.flab.commerce.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Length(min = 2, max = 30)
  private String name;

  @NotBlank
  @Length(min = 3, max = 5)
  private String zipcode;

  @NotBlank
  @Length(min = 10, max = 40)
  private String address;

  @NotBlank
  @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$") // 010-1234-5678
  private String phone;

  @NotBlank
  @Length(min = 8, max = 15)
  private String password;
}
