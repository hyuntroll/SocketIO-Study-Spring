package com.example.socketiostudyspring.service;


import com.corundumstudio.socketio.SocketIOServer;
import com.example.socketiostudyspring.model.Typing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypingStatusService {

    private final SocketIOServer server;
    private final Map<String, Long> typingMap = new ConcurrentHashMap<>();

    public void updateTying(String userId) {
        typingMap.put(userId, System.currentTimeMillis());
    }

    public boolean isTyping(String userId) {
        return typingMap.containsKey(userId);
    }

    public void removeTyping(String userId) {
        if (isTyping(userId)) {
            log.info("typing remove session: " + userId);

            typingMap.remove(userId);
            Typing data = new Typing(userId,false);

            server.getAllClients().forEach(client -> {
                client.sendEvent("typing", data);
            });
        }
    }

    @Scheduled(fixedRate = 1000)
    public void updateTypingStatus() {
        long now = System.currentTimeMillis();
        typingMap.forEach((userId, type) -> {
            if (now - typingMap.get(userId) > 1000) {
                removeTyping(userId);
            }
        });
    }
}
