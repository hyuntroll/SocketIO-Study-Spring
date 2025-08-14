package com.example.socketiostudyspring.service;


import com.corundumstudio.socketio.SocketIOServer;
import com.example.socketiostudyspring.model.Typing;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TypingStatusService {

    private final SocketIOServer server;
    private final Map<String, Long> typingMap = new ConcurrentHashMap<>();

    public void updateTying(String userId) {
        typingMap.put(userId, System.currentTimeMillis());
    }

    public boolean isTyping(String userId) {
        return typingMap.containsKey(userId);
    }

    @Scheduled(fixedRate = 1000)
    public void updateTypingStatus() {
        System.out.println("테스트1");
        long now = System.currentTimeMillis();
        typingMap.forEach((userId, type) -> {
            if (now - typingMap.get(userId) > 4000) {
                typingMap.remove(userId);
                // socket.send
                Typing data = new Typing(userId,false);

                server.getAllClients().forEach(client -> {
                    if (!client.getSessionId().equals(client.getSessionId())) {
                        client.sendEvent("typing", data);
                    }
                });
            }
        });
    }
}
