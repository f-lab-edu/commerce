package com.flab.commerce.domain.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String phone;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;
}
