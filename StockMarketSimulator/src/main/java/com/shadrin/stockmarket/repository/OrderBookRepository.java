package com.shadrin.stockmarket.repository;

import com.shadrin.stockmarket.model.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderBookRepository extends JpaRepository<OrderBook, String> {

}
