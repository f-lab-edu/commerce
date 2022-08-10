package com.flab.commerce.domain.store;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {

    int register(Store newStore);
}
