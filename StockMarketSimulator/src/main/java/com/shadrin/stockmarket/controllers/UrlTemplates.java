package com.shadrin.stockmarket.controllers;

public final class UrlTemplates {
    public static final String PING = "/ping";

    public static final String ORDER_ADD = "/order/add";
    public static final String ORDER_CANCEL = "/order/cancel/{id}";

    public static final String SYMBOL_ADD = "/symbol/add/{symbol}";

    public static final String WS_TRADES_ENDPOINT = "/trades-ws";

    private UrlTemplates() {

    }
}
