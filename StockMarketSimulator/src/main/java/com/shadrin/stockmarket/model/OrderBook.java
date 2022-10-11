package com.shadrin.stockmarket.model;

import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
public class OrderBook {
    @Id
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid symbol value")
    private String symbol;

    public OrderBook(String symbol) {
        if (StringUtils.hasText(symbol)) {
            this.symbol = symbol;
        } else {
            throw new IllegalArgumentException("Non empty symbol is required");
        }
    }

    public OrderBook() {}

    public String getSymbol() {
        return symbol;
    }
}
