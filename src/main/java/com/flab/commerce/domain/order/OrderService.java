package com.flab.commerce.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.delivery.Delivery;
import com.flab.commerce.domain.delivery.DeliveryMapper;
import com.flab.commerce.domain.delivery.DeliveryStatus;
import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuMapper;
import com.flab.commerce.domain.option.Option;
import com.flab.commerce.domain.option.OptionMapper;
import com.flab.commerce.domain.optiongroup.OptionGroup;
import com.flab.commerce.domain.optiongroup.OptionGroupMapper;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final MenuMapper menuMapper;

  private final OptionMapper optionMapper;

  private final OrderMapper orderMapper;

  private final DeliveryMapper deliveryMapper;

  private final OptionGroupMapper menuOptionGroupMapper;

  private final ObjectMapper objectMapper;

  public void save(Long userId, OrderSaveDto saveDto) throws JsonProcessingException {
    List<OrderMenuSaveDto> orderMenuSaveDtos = getOrderMenuSaveDtos(saveDto.getMenus());

    BigInteger totalPrice = orderMenuSaveDtos.stream()
        .map(OrderMenuSaveDto::getTotalPrice)
        .reduce(BigInteger.ZERO, BigInteger::add);

    Orders order = createOrder(userId, orderMenuSaveDtos, totalPrice);
    orderMapper.save(order);

    Delivery delivery = createDelivery(saveDto, order.getId());
    deliveryMapper.save(delivery);
  }

  private List<OrderMenuSaveDto> getOrderMenuSaveDtos(List<OrderMenuDto> orderMenuDtos) {
    Set<Long> menuIds = orderMenuDtos.stream()
        .map(OrderMenuDto::getMenuId)
        .collect(Collectors.toSet());
    List<Menu> menus = menuMapper.findByIdIn(menuIds);

    Set<Long> optionIds = orderMenuDtos.stream()
        .map(OrderMenuDto::getOptionIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
    List<Option> options = optionMapper.findByIdIn(optionIds);

    Set<Long> optionGroupIds = options.stream()
        .map(Option::getOptionGroupId)
        .collect(Collectors.toSet());
    List<OptionGroup> optionGroups = menuOptionGroupMapper.findByIdIn(optionGroupIds);

    List<OrderMenuSaveDto> orderMenuSaveDtos = new ArrayList<>(orderMenuDtos.size());
    for (OrderMenuDto orderMenuDto : orderMenuDtos) {
      for (Menu menu : menus) {
        if (orderMenuDto.getMenuId().equals(menu.getId())) {
          List<OrderMenuOptionSaveDto> orderMenuOptionSaveDtos = getOrderMenuOptionSaveDtos(
              options, optionGroups, orderMenuDto);

          BigInteger menuTotalPrice = getMenuTotalPrice(orderMenuDto, menu,
              orderMenuOptionSaveDtos);

          OrderMenuSaveDto orderMenuSaveDto = OrderMenuSaveDto.builder()
              .name(menu.getName())
              .price(menu.getPrice())
              .totalPrice(menuTotalPrice)
              .amount(orderMenuDto.getAmount())
              .orderMenuOptionSaveDtos(orderMenuOptionSaveDtos)
              .build();

          orderMenuSaveDtos.add(orderMenuSaveDto);
        }
      }
    }
    return orderMenuSaveDtos;
  }

  private BigInteger getMenuTotalPrice(OrderMenuDto orderMenuDto, Menu menu,
      List<OrderMenuOptionSaveDto> orderMenuOptionSaveDtos) {
    BigInteger menuTotalPrice = orderMenuOptionSaveDtos.stream()
        .map(OrderMenuOptionSaveDto::getPrice)
        .reduce(BigInteger.ZERO, BigInteger::add);
    menuTotalPrice = menuTotalPrice.add(menu.getPrice());
    menuTotalPrice = menuTotalPrice.multiply(BigInteger.valueOf(orderMenuDto.getAmount()));
    return menuTotalPrice;
  }

  private List<OrderMenuOptionSaveDto> getOrderMenuOptionSaveDtos(List<Option> options,
      List<OptionGroup> optionGroups, OrderMenuDto orderMenuDto) {
    List<OrderMenuOptionSaveDto> orderMenuOptionSaveDtos = new LinkedList<>();

    for (Long optionId : orderMenuDto.getOptionIds()) {
      for (Option option : options) {
        if (optionId.equals(option.getId())) {
          for (OptionGroup optionGroup : optionGroups) {
            if (optionGroup.getId().equals(option.getOptionGroupId())) {
              OrderMenuOptionSaveDto orderMenuOptionSaveDto = OrderMenuOptionSaveDto.builder()
                  .optionGroupName(optionGroup.getName())
                  .optionName(option.getName())
                  .price(option.getPrice())
                  .build();
              orderMenuOptionSaveDtos.add(orderMenuOptionSaveDto);
            }
          }
        }
      }
    }
    return orderMenuOptionSaveDtos;
  }

  private Orders createOrder(Long userId, List<OrderMenuSaveDto> menuSaveDtos,
      BigInteger totalPrice) throws JsonProcessingException {
    final String menuOptions = objectMapper.writeValueAsString(menuSaveDtos);

    return Orders.builder()
        .userId(userId)
        .totalPrice(totalPrice)
        .status(OrderStatus.READY)
        .menuOptions(menuOptions)
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();
  }

  private Delivery createDelivery(OrderSaveDto saveDto, Long orderId) {
    return Delivery.builder()
        .orderId(orderId)
        .zipcode(saveDto.getZipCode())
        .address(saveDto.getAddress())
        .addressDetail(saveDto.getAddressDetail())
        .phone(saveDto.getPhone())
        .status(DeliveryStatus.READY)
        .build();
  }
}
