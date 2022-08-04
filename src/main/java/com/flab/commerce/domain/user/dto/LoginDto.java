package com.flab.commerce.domain.user.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@Setter
public class LoginDto {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Length(min = 8, max = 15)
  private String password;

  @NotBlank
  private String uri;
}
