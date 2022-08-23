package com.flab.commerce.domain.optiongroup;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OptionGroupReadDto {

  private Long id;

  private String name;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
