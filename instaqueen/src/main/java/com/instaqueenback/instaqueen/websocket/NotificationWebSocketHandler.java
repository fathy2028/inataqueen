package com.instaqueenback.instaqueen.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("WebSocket connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        log.info("WebSocket disconnected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Client can send ping/auth messages - we just acknowledge
        log.debug("Received from {}: {}", session.getId(), message.getPayload());
    }

    /**
     * Broadcast a notification to ALL connected clients.
     */
    public void broadcast(Map<String, Object> notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            TextMessage message = new TextMessage(json);
            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        log.error("Failed to send to session {}: {}", session.getId(), e.getMessage());
                    }
                }
            }
            log.info("Broadcasted notification to {} clients", sessions.size());
        } catch (Exception e) {
            log.error("Failed to serialize notification: {}", e.getMessage());
        }
    }

    /**
     * Get the number of currently connected clients.
     */
    public int getConnectedCount() {
        return sessions.size();
    }
}
