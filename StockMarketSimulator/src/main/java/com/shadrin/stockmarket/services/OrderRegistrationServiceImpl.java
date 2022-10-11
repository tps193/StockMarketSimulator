package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.dto.OrderDto;
import com.shadrin.stockmarket.events.LogEvent;
import com.shadrin.stockmarket.model.Order;
import com.shadrin.stockmarket.model.OrderBook;
import com.shadrin.stockmarket.model.OrderState;
import com.shadrin.stockmarket.repository.OrderBookRepository;
import com.shadrin.stockmarket.repository.OrderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderRegistrationServiceImpl implements OrderRegistrationService {
    private final Log log = LogFactory.getLog(this.getClass());

    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final OrderBookLocker orderBookLocker;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderRegistrationServiceImpl(
            @Autowired OrderRepository orderRepository,
            @Autowired OrderBookRepository orderBookRepository,
            @Autowired OrderBookLocker orderBookLocker,
            @Autowired ApplicationEventPublisher applicationEventPublisher
    ) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.orderBookLocker = orderBookLocker;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    @Override
    public Order addNewOrder(OrderDto orderDto) {
        var orderBookOptional = orderBookRepository.findById(orderDto.getSymbol());
        if (orderBookOptional.isPresent()) {
            var orderBook = orderBookOptional.get();
            var lock = orderBookLocker.getOrderBookLock(orderBook);
            lock.lock();
            try {
                var order = new Order(
                        orderBook,
                        orderDto.getPrice(),
                        orderDto.getCount(),
                        orderDto.getType()
                );
                orderRepository.save(order);
                log.info(String.format("%s is created successfully", order.prettyPrint()));
                return order;
            } finally {
                lock.unlock();
            }
        } else {
            log.error(String.format("Error adding order %s for non existing symbol %s", orderDto, orderDto.getSymbol()));
            throw new RuntimeException(String.format("%s symbol doesn't exist.", orderDto.getSymbol()));
        }
    }

    @Transactional
    @Override
    public void cancelOrder(long id) {
        var orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            var order = orderOptional.get();
            var orderBook = order.getOrderBook();
            var lock = orderBookLocker.getOrderBookLock(orderBook);
            lock.lock();
            try {
                if (order.getState() == OrderState.ACTIVE) {
                    order.setState(OrderState.CANCELLED);
                    var event = new LogEvent(String.format("%s is cancelled", order.prettyPrint()), log);
                    applicationEventPublisher.publishEvent(event);
                } else {
                    log.error(String.format("Error cancelling order with ID: %d as it is already %s", id, order.getState()));
                    throw new RuntimeException(String.format("Order with ID: %d can't be cancelled as it is in terminal state %s", id, order.getState()));
                }
            } finally {
                lock.unlock();
            }
        } else {
            log.error(String.format("Attempt to cancel non existing order with ID: %d", id));
            throw new RuntimeException(String.format("Order with ID: %d doesn't exist", id));
        }
    }

    @Transactional
    @Override
    public void addNewSymbol(String symbol) {
        if (symbol != null && !symbol.isBlank()) {
            if (!orderBookRepository.existsById(symbol)) {
                orderBookRepository.save(new OrderBook(symbol));
            } else {
                throw new RuntimeException(String.format("Symbol %s already exists", symbol));
            }
        }
    }
}
