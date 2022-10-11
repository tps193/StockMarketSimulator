package com.shadrin.stockmarket.events;

import com.shadrin.stockmarket.services.TradeLeger;
import com.shadrin.stockmarket.services.WebSocketHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TradeEventListener {
    private final Log tradeLegerLog = LogFactory.getLog(TradeLeger.class);

    private final WebSocketHandler webSocketHandler;

    public TradeEventListener(@Autowired WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcastTrade(TradeEvent tradeEvent) {
        webSocketHandler.broadcastTrade(tradeEvent.getTrade());
        tradeLegerLog.info(String.format("New %s registered", tradeEvent.getTrade().prettyPrint()));
    }
}
