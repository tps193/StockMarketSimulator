package com.shadrin.stockmarket.cli.websocket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AppWebSocketListener extends WebSocketListener {
    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        System.out.println("Websocket connection established");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        System.out.printf("[%s] %s%n", new Date(System.currentTimeMillis()), text);
    }
}
