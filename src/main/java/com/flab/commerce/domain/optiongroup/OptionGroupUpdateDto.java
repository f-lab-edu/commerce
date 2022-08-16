package com.flab.commerce.domain.optiongroup;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OptionGroupUpdateDto {

  @NotBlank
  @Length(min = 2, max = 100)
  private String name;
}
