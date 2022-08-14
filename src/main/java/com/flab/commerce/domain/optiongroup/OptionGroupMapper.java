package com.flab.commerce.domain.optiongroup;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionGroupMapper {

  int save(OptionGroup optionGroup);

  List<OptionGroup> findByStoreId(Long storeId);

  int delete(Long id);

  boolean idExists(Long id);

  boolean idAndStoreIdExists(Long id, Long storeId);
}
