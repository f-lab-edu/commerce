package com.flab.commerce.domain.price;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PriceMapper {

  int save(Price newPrice);
}
