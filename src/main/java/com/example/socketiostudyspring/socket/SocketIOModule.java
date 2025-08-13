package com.example.socketiostudyspring.socket;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.socketiostudyspring.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SocketIOModule {

    private final SocketIOServer server;

    public SocketIOModule(SocketIOServer server) {
        this.server = server;
        server.addConnectListener(listenConnected());
        server.addDisconnectListener(listenDisconnected());
        server.addEventListener("message", Message.class, chatReceiver());
    }

    /**
     * 클라이언트 연결 리스너
     */
    public ConnectListener listenConnected() {
        return (client) -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            log.info("connect:" + params.toString() + client.getSessionId().toString());
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


}
