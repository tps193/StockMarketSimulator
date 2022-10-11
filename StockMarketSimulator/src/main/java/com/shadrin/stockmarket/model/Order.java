package com.shadrin.stockmarket.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol")
    private OrderBook orderBook;

    @Column
    @Min(value = 0)
    private int price;
    @Column
    @Min(value = 0)
    private int count;
    @Column
    @NotNull
    private OrderType type;
    @Column
    @NotNull
    private OrderState state;

    public Order() {}

    public String getSymbol() {
        return orderBook.getSymbol();
    }

    public Order(OrderBook orderBook, int price, int count, OrderType type) {
        this.orderBook = orderBook;
        this.price = price;
        this.count = count;
        this.type = type;
        this.state = OrderState.ACTIVE;
    }

    public long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OrderType getType() {
        return type;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public enum OrderType {
        BUY,
        SELL
    }

    public String prettyPrint() {
        return String.format("Order {ID=%d, %s %s %d @ %d}", id, orderBook.getSymbol(), type, count, price);
    }
}
