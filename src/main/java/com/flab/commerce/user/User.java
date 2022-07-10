package com.flab.commerce.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String id;
  private String email;
  private String password;
  private String name;
  private String phone;
  private String zipcode;
  private String address;
  private LocalDateTime createDateTime;
  private LocalDateTime modifyDateTime;
}
