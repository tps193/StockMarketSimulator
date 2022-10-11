package com.shadrin.stockmarket.events;

import com.shadrin.stockmarket.model.Trade;
import org.springframework.context.ApplicationEvent;

public class TradeEvent extends ApplicationEvent {
    private final Trade trade;

    public TradeEvent(Trade trade) {
        super(trade);
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }
}
