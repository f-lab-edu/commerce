package com.flab.commerce.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.delivery.Delivery;
import com.flab.commerce.domain.delivery.DeliveryMapper;
import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final MenuMapper menuMapper;

  private final OrderMapper orderMapper;

  private final DeliveryMapper deliveryMapper;

  private final ObjectMapper objectMapper;


  public Orders save(OrderSaveDto saveDto) throws JsonProcessingException {
    Menu menu = menuMapper.findById(saveDto.getMenuId());

    Orders order = createOrder(saveDto, menu);
    orderMapper.save(order);

    Delivery delivery = createDelivery(saveDto, order);
    deliveryMapper.save(delivery);

    return order;
  }

  private Orders createOrder(OrderSaveDto saveDto, Menu menu) throws JsonProcessingException {
    final String menuOptions = objectMapper.writeValueAsString(menu.getMenuOptions());
    final BigDecimal totalPrice = menu.calculateTotalPrice(saveDto.getAmount());

    return Orders.builder()
        .userId(saveDto.getUserId())
        .totalPrice(totalPrice)
        .status(OrderStatus.READY)
        .address(saveDto.getAddress())
        .menuOptions(menuOptions)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();
  }

  private Delivery createDelivery(OrderSaveDto saveDto, Orders order) {
    return Delivery.builder()
        .orderId(order.getId())
        .address(saveDto.getAddress())
        .addressDetail(saveDto.getAddressDetail())
        .zip(saveDto.getZipCode())
        .phone(saveDto.getPhone())
        .build();
  }
}
