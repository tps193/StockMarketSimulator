package com.shadrin.stockmarket.repository;

import com.shadrin.stockmarket.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

}
