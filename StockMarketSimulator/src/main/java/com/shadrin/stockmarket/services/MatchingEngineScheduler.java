package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.repository.OrderBookRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchingEngineScheduler {
    private final Log log = LogFactory.getLog(this.getClass());

    private final MatchingEngine matchingEngine;
    private final OrderBookRepository orderBookRepository;

    public MatchingEngineScheduler(
        @Autowired MatchingEngine matchingEngine,
        @Autowired OrderBookRepository orderBookRepository
    ) {
        this.matchingEngine = matchingEngine;
        this.orderBookRepository = orderBookRepository;
    }

    @Scheduled(fixedDelay = 1000)
    public void balance() {
        log.debug("Order books balance started.");
        orderBookRepository.findAll().parallelStream().forEach(orderBook -> {
            try {
                log.info(String.format("Start balancing the '%s' order book", orderBook.getSymbol()));
                matchingEngine.balanceBook(orderBook);
                log.info(String.format("Finished balancing the '%s' order book", orderBook.getSymbol()));
            } catch (Exception e) {
                log.error(String.format("Error balancing the '%s' order book", orderBook.getSymbol()), e);
            }
        });
        log.debug("Order books balance finished.");
    }
}
