package com.flab.commerce.domain.delivery;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeliveryMapper {

  int save(Delivery newDelivery);

  Delivery findById(Long id);
}
