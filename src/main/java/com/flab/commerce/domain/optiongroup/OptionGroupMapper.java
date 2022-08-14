package com.flab.commerce.domain.optiongroup;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionGroupMapper {

  int save(OptionGroup optionGroup);
}
