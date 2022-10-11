package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.dto.OrderDto;
import com.shadrin.stockmarket.model.Order;

public interface OrderRegistrationService {
    Order addNewOrder(OrderDto order);
    void cancelOrder(long id);

    void addNewSymbol(String symbol);
}
