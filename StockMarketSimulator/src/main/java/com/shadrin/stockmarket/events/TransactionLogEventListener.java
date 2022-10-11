package com.shadrin.stockmarket.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
public class TransactionLogEventListener {
    private final Log defaultLog = LogFactory.getLog(this.getClass());

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void logInfo(LogEvent logEvent) {
        var log = Optional.ofNullable(logEvent.getLog()).orElse(defaultLog);
        log.info(logEvent.getSource());
    }
}
