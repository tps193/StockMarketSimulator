package com.shadrin.stockmarket.services;

import com.shadrin.stockmarket.model.Trade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final Log log = LogFactory.getLog(this.getClass());

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Open websocket connection with session id " + session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Close websocket connection with session id " + session.getId());
        sessions.remove(session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        log.info("Close websocket connection with session id " + session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error(String.format("Websocket connection error for session %s", session.getId()), throwable);
    }

    public void broadcastTrade(Trade trade) {
        String message = trade.toString();
        sessions.forEach(session -> {
            synchronized (session) {
                try {
                    log.debug(String.format("Broadcast %s to session %s", trade.prettyPrint(), session.getId()));
                    session.sendMessage(new TextMessage(String.format("New %s registered", trade.prettyPrint())));
                } catch (IOException e) {
                    log.error(String.format("Error broadcasting message '%s' for session %s", message, session.getId()), e);
                }
            }
        });
    }
}
