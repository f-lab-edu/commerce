package com.flab.commerce.domain.user;

import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  private Long id;

  private String email;

  private String password;

  private String name;

  private String phone;

  private String zipcode;

  private String address;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
