package com.flab.commerce.domain.order;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

  int save(Orders orders);

}
