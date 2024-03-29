package com.flab.commerce.domain.owner;

import com.flab.commerce.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRegisterDto {

    @Email
    @NotBlank
    private String email;

    @Length(min = 8)
    @NotBlank
    private String password;

    @Length(min = 2)
    @NotBlank
    private String name;

    @Length(min = 10)
    @NotBlank
    private String phone;

    public Owner toOwner() {
        return Owner.builder()
                .email(email)
                .password(Utils.encodePassword(password))
                .name(name)
                .phone(phone)
                .createDateTime(ZonedDateTime.now())
                .modifyDateTime(ZonedDateTime.now())
                .build();
    }
}
