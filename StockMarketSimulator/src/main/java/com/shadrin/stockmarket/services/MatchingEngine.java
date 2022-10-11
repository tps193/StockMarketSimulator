package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.events.LogEvent;
import com.shadrin.stockmarket.events.TradeEvent;
import com.shadrin.stockmarket.model.Order;
import com.shadrin.stockmarket.model.OrderBook;
import com.shadrin.stockmarket.model.OrderState;
import com.shadrin.stockmarket.model.Trade;
import com.shadrin.stockmarket.repository.OrderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MatchingEngine {
    private final Log log = LogFactory.getLog(this.getClass());

    private final TradeLeger tradeLeger;
    private final OrderBookLocker orderBookLocker;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepository;

    public MatchingEngine(
            @Autowired TradeLeger tradeLeger,
            @Autowired OrderRepository orderRepository,
            @Autowired OrderBookLocker orderBookLocker,
            @Autowired ApplicationEventPublisher applicationEventPublisher
    ) {
        this.tradeLeger = tradeLeger;
        this.orderRepository = orderRepository;
        this.orderBookLocker = orderBookLocker;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void balanceBook(OrderBook orderBook) {
        var lock = orderBookLocker.getOrderBookLock(orderBook);
        lock.lock();
        try {
            var buyOrders = orderRepository.findActiveBuyOrders(orderBook);
            var sellOrders = orderRepository.findActiveSellOrders(orderBook);
            balanceOrders(buyOrders, sellOrders);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void balanceOrders(List<Order> buyOrders, List<Order> sellOrders) {
        for(var buyOrder : buyOrders) {
            while(buyOrder.getState() == OrderState.ACTIVE) {
                var sellOrderOptional = sellOrders.stream().filter(order -> order.getState() == OrderState.ACTIVE).findFirst();
                if (sellOrderOptional.isPresent()) {
                    var sellOrder = sellOrderOptional.get();
                    if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                        int totalPrice = sellOrder.getPrice();
                        int totalCount = Math.min(buyOrder.getCount(), sellOrder.getCount());
                        var trade = new Trade(
                                buyOrder,
                                sellOrder,
                                totalPrice,
                                totalCount);
                        tradeLeger.submitTrade(trade);
                        applicationEventPublisher.publishEvent(new TradeEvent(trade));
                        updateOrderState(buyOrder, totalCount);
                        updateOrderState(sellOrder, totalCount);
                    } else {
                        break;
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void updateOrderState(Order order, int tradeQuantity) {
        order.setCount(order.getCount() - tradeQuantity);
        String message;
        if (order.getCount() > 0) {
            message = String.format("Order %d is partially filled. New order state is %s", order.getId(), order.prettyPrint());
        } else {
            order.setState(OrderState.CLOSED);
            message = String.format("Order %d is fully filled. Order %d is closed.", order.getId(), order.getId());
        }
        var event = new LogEvent(message, log);
        applicationEventPublisher.publishEvent(event);
    }
}
