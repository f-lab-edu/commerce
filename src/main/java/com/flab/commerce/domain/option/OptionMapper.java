package com.flab.commerce.domain.option;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionMapper {

  int save(Option option);

  Option findById(Long id);

  int update(Option option);

  boolean idExists(Long id);

  boolean idAndOptionGroupIdExists(Long id, Long optionGroupId);

  int delete(Long id);
}
