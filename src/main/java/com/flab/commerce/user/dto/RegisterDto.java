package com.flab.commerce.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    @Length(min = 3, max = 5)
    private String zipcode;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
}
