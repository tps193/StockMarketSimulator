package com.shadrin.stockmarket.dto;

import com.shadrin.stockmarket.model.OrderType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderDto {
    @NotEmpty
    private String symbol;
    @Min(value = 1)
    private int price;
    @Min(value = 1)
    private int count;
    @NotNull
    private OrderType type;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public void setType(OrderType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", type=" + type +
                '}';
    }
}
