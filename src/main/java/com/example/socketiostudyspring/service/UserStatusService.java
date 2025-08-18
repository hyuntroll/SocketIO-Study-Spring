package com.example.socketiostudyspring.service;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.socketiostudyspring.model.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserStatusService {

    public void notifyConnection(SocketIOClient client, SocketIOServer server, String namespace) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        log.info("connect: {} {}", params.toString(), client.getSessionId().toString());

        UserStatus userStatus = UserStatus.builder()
                .userId(client.getSessionId().toString())
                .connectStatus("connect")
                .build();
        server.addNamespace(namespace).getAllClients().forEach( c -> {
            if (!client.getSessionId().equals(c.getSessionId())) {
                c.sendEvent("user_status", userStatus);
            }

        });
    }

    public void notifyDisconnection(SocketIOClient client, SocketIOServer server, String namespace) {
        String sessionId = client.getSessionId().toString();
        log.info("disconnect: {}", sessionId);

        UserStatus userStatus = UserStatus.builder()
                .userId(client.getSessionId().toString())
                .connectStatus("disconnect")
                .build();
        server.getAllClients().forEach( c -> {
            if (!client.getSessionId().equals(c.getSessionId())) {
                c.sendEvent("user_status", userStatus);
            }

        });
        client.disconnect();
    }






}
