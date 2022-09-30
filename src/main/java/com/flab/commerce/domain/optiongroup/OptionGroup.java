package com.flab.commerce.domain.optiongroup;

import com.flab.commerce.domain.option.Option;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionGroup {

  private Long id;

  private String name;

  private Long storeId;

  private List<Option> options;

  private ZonedDateTime createDateTime;

  private ZonedDateTime modifyDateTime;
}
