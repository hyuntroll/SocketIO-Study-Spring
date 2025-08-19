package com.example.socketiostudyspring.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.socketiostudyspring.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final SocketIOServer server;

    private final TypingStatusService typingStatusService;

    public void chatHandler(SocketIOClient client, Message data, AckRequest ackSender) {
        String userId = client.getSessionId().toString();
        log.info("{}: {}", userId, data.getMessage());

        typingStatusService.removeTyping(userId);

        server.getBroadcastOperations().sendEvent("message", client, data);

        if (ackSender.isAckRequested()) {
            ackSender.sendAckData("ok");
        }
    }
}
