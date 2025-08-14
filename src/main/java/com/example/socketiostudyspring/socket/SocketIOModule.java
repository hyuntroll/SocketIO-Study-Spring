package com.example.socketiostudyspring.socket;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.socketiostudyspring.model.Message;
import com.example.socketiostudyspring.model.Typing;
import com.example.socketiostudyspring.model.UserStatus;
import com.example.socketiostudyspring.service.TypingStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SocketIOModule {

    private final SocketIOServer server;
    private final TypingStatusService typingStatusService;

    public SocketIOModule(SocketIOServer server, TypingStatusService typingStatusService) {
        this.server = server;
        this.typingStatusService = typingStatusService;
        server.addConnectListener(listenConnected());
        server.addDisconnectListener(listenDisconnected());
        server.addEventListener("message", Message.class, chatReceiver());
        server.addEventListener("typing", Typing.class, receiveTyping());
    }

    /**
     * 클라이언트 연결 리스너
     */
    public ConnectListener listenConnected() {
        return (client) -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            log.info("connect:" + params.toString() + client.getSessionId().toString());

            server.getAllClients().forEach( c -> {
                if (!client.getSessionId().equals(c.getSessionId())) {
                    UserStatus userStatus = UserStatus.builder()
                            .userId(client.getSessionId().toString())
                            .connectStatus("connect")
                            .build();
                    c.sendEvent("user_status", userStatus);
                }

            });
        };
    }

    /**
     * 클라이언트 연결 해제 리스너
     */
    public DisconnectListener listenDisconnected() {
        return client -> {
            String sessionId = client.getSessionId().toString();
            log.info("disconnect: " + sessionId);
            client.disconnect();

            server.getAllClients().forEach( c -> {
                if (!client.getSessionId().equals(c.getSessionId())) {
                    UserStatus userStatus = UserStatus.builder()
                            .userId(client.getSessionId().toString())
                            .connectStatus("disconnect")
                            .build();
                    c.sendEvent("user_status", userStatus);
                }

            });
        };
    }

    public DataListener<Message> chatReceiver() {
        return (client, data, ackSender) -> {
            log.info("chat received: " + data.getMessage());

            server.getAllClients().forEach(c -> {
                if (!client.getSessionId().equals(c.getSessionId()))
                {
//                    System.out.println(c.getSessionId() + ": " + data.getMessage());
                    c.sendEvent("message", data);
                }
            });

        };
    }

    public DataListener<Typing> receiveTyping() {
        return (client, data, ackSender) -> {


        };
    }


}
