package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.model.Trade;
import com.shadrin.stockmarket.repository.TradeRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TradeLeger {
    private final Log log = LogFactory.getLog(this.getClass());

    private final TradeRepository tradeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TradeLeger(
            @Autowired TradeRepository tradeRepository,
            @Autowired ApplicationEventPublisher applicationEventPublisher
    ) {
        this.tradeRepository = tradeRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void submitTrade(Trade trade) {
        log.debug("Saving trade to database: " + trade);
        tradeRepository.save(trade);
        applicationEventPublisher.publishEvent(trade);
    }
}
