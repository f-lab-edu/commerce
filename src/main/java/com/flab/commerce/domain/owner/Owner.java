package com.flab.commerce.domain.owner;

import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner implements Serializable {

  private Long id;

  private String email;

  private String password;

  private String name;

  private String phone;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
