package com.shadrin.stockmarket.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 1)
    private int price;
    @Min(value = 1)
    private int count;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Order buyOrder;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Order sellOrder;

    public Trade(Order buyOrder, Order sellOrder, int price, int count) {
        if (buyOrder != null && sellOrder != null) {
            if (!buyOrder.getSymbol().equals(sellOrder.getSymbol())) {
                throw new IllegalArgumentException("Orders must belong same order book");
            }
        }
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.price = price;
        this.count = count;
    }

    public Trade() {}

    public String prettyPrint() {
        return String.format("Trade {ID=%d, %s %d @ %d (%s order %d and %s order %d)}",
                id,
                buyOrder.getSymbol(),
                count,
                price,
                buyOrder.getType(),
                buyOrder.getId(),
                sellOrder.getType(),
                sellOrder.getId());
    }
}
