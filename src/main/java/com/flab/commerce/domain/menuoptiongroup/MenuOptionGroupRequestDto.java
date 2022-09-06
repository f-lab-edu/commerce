package com.flab.commerce.domain.menuoptiongroup;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionGroupRequestDto {

  @NotNull
  @Min(0)
  private Long menuId;

  @NotNull
  @Min(0)
  private List<Long> optionGroupIds;
}
