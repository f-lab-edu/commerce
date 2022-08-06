package com.flab.commerce.domain.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

  private Long id;

  private String recipient;

  private String address;

  private String addressDetail;

  private String zip;

  private String phone;

  private DeliveryStatus status;

  private Long orderId;

  private Long riderId;
}
