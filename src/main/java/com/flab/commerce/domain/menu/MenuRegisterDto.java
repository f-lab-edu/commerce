package com.flab.commerce.domain.menu;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRegisterDto {

  @NotBlank
  @Length(min = 2, max = 20)
  private String name;

  @Min(0)
  @Max(10_000_000)
  @NotNull
  private Long price;

  @NotBlank
  @Length(min = 40, max = 41)
  private String image;
}
