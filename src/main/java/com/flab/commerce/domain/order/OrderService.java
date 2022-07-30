package com.flab.commerce.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.menu.Menu;
import com.flab.commerce.domain.menu.MenuMapper;
import com.flab.commerce.domain.menu.MenuOption;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final MenuMapper menuMapper;

  private final OrderMapper orderMapper;

  private final ObjectMapper objectMapper;

  public Orders save(OrderSaveDto saveDto) throws JsonProcessingException {
    Menu menu = menuMapper.findById(saveDto.getMenuId());

    List<MenuOption> options = menu.getGroups().stream()
        .map(group -> group.getOptions())
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    BigDecimal totalPrice = menu.getPrice().multiply(new BigDecimal(saveDto.getAmount()));

    Orders order = Orders.builder()
        .userId(saveDto.getUserId())
        .totalPrice(totalPrice)
        .status(OrderStatus.READY)
        .address(saveDto.getAddress())
        .menuOptions(objectMapper.writeValueAsString(options))
        .createDateTime(LocalDateTime.now())
        .updateDateTime(LocalDateTime.now())
        .build();

    orderMapper.save(order);

    return order;
  }
}
