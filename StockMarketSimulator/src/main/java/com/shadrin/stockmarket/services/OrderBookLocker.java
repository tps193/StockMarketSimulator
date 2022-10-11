package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.model.OrderBook;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OrderBookLocker {
    private final Map<String, Lock> locks = Collections.synchronizedMap(new HashMap<>());

    public Lock getOrderBookLock(OrderBook orderBook) {
        return locks.computeIfAbsent(orderBook.getSymbol(), (k) -> new ReentrantLock(true));
    }
}
