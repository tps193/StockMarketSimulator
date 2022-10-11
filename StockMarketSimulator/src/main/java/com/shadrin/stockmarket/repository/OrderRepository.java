package com.shadrin.stockmarket.repository;

import com.shadrin.stockmarket.model.Order;
import com.shadrin.stockmarket.model.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderBook = :orderBook AND o.state = com.shadrin.stockmarket.model.OrderState.ACTIVE AND o.type = com.shadrin.stockmarket.model.OrderType.BUY ORDER BY o.id")
    List<Order> findActiveBuyOrders(@Param("orderBook") OrderBook orderBook);

    @Query("SELECT o FROM Order o WHERE o.orderBook = :orderBook AND o.state = com.shadrin.stockmarket.model.OrderState.ACTIVE AND o.type = com.shadrin.stockmarket.model.OrderType.SELL ORDER BY o.price, o.id")
    List<Order> findActiveSellOrders(@Param("orderBook") OrderBook orderBook);
}
