package com.flab.commerce.domain.menuoptiongroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuOptionGroupResponseDto {

  private Long optionGroupId;
  private String optionGroupName;
  private String menuNames;
}

