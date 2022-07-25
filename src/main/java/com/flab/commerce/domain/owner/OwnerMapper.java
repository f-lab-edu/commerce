package com.flab.commerce.domain.owner;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper {

    int register(Owner newOwner);

    Owner findByEmail(String email);
}
