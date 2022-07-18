package com.flab.commerce.domain.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRegisterDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    @Length(min = 2)
    private String name;

    @NotBlank
    @Length(min = 11)
    private String phone;

    public Owner toOwner() {
        return Owner.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
    }
}
