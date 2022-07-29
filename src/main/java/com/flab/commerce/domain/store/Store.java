package com.flab.commerce.domain.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    private Long id;

    private String name;

    private String phone;

    private String address;

    private StoreStatus status;

    private String description;

    private String image;

    private Long ownerId;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;
}
