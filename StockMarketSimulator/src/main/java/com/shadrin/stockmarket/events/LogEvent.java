package com.shadrin.stockmarket.events;

import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationEvent;

public class LogEvent extends ApplicationEvent {
    private final Log log;

    public LogEvent(String message, Log log) {
        super(message);
        this.log = log;
    }

    public Log getLog() {
        return log;
    }
}
