package com.shadrin.stockmarket.repository;

import com.shadrin.stockmarket.model.Order;
import com.shadrin.stockmarket.model.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderBook = :orderBook AND o.state = com.shadrin.stockmarket.model.OrderState.ACTIVE AND o.type = :type ")
    List<Order> findActiveOrders(@Param("orderBook") OrderBook orderBook, @Param("type") Order.OrderType type);
}
