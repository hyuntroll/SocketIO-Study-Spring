package com.example.socketiostudyspring.socket;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.socketiostudyspring.model.*;
import com.example.socketiostudyspring.service.ChatRoomService;
import com.example.socketiostudyspring.service.ChatService;
import com.example.socketiostudyspring.service.TypingStatusService;
import com.example.socketiostudyspring.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SocketIOModule {

    private final SocketIOServer server;
    private final TypingStatusService typingStatusService;
    private final ChatRoomService chatRoomService;
    private final UserStatusService userStatusService;
    private final ChatService chatService;

    public SocketIOModule(SocketIOServer server, TypingStatusService typingStatusService, ChatRoomService chatRoomService, UserStatusService userStatusService, ChatService chatService) {
        this.server = server;
        this.typingStatusService = typingStatusService;
        this.chatRoomService = chatRoomService;
        this.userStatusService = userStatusService;
        this.chatService = chatService;

        // add Handler
        // namespace - "/"
        server.addConnectListener(listenConnected("/"));
        server.addDisconnectListener(listenDisconnected("/"));
        server.addEventListener("message", Message.class, chatReceiver());

//        server.addEventListener("typing", Typing.class, receiveTyping());
//        server.addEventListener("message", Message.class, chatReceiver());
//        server.addEventListener("leave-room", RoomRequestDTO.class, leaveRoom());

        // namespace - "/room"
        server.addNamespace("/room").addConnectListener(listenConnected("/room"));
        server.addNamespace("/room").addDisconnectListener(listenDisconnectedFromRoom());
        server.addNamespace("/room").addEventListener("message", Message.class, chatReceiverFromRoom());
        server.addNamespace("/room").addEventListener("join-room", RoomRequestDTO.class, joinRoom());
        server.addNamespace("/room").addEventListener("leave-room", RoomRequestDTO.class, leaveRoom());


    }

    /**
     * 클라이언트 연결 리스너
     */
    public ConnectListener listenConnected(String namespace) {
        return client -> userStatusService.notifyConnection(client, namespace);
    }

    /**
     * 클라이언트 연결 해제 리스너
     */
    public DisconnectListener listenDisconnected(String namespace) {
        return client -> userStatusService.notifyDisconnection(client, namespace);
    }

    public DisconnectListener listenDisconnectedFromRoom() {
        return client -> {
          userStatusService.notifyDisconnection(client, "/room");
          chatRoomService.handleDisconnectFromRooms(client.getSessionId().toString());
        };
    }


    public DataListener<Message> chatReceiver() {
        return chatService::chatHandler;
    }

    public DataListener<Message> chatReceiverFromRoom() {
        return chatRoomService::chatHandle;
    }

    public DataListener<Typing> receiveTyping() {
        return (client, data, ackSender) -> {
            log.info("typing session: {}", client.getSessionId().toString());


            if (!typingStatusService.isTyping(client.getSessionId().toString())) {

                server.getAllClients().forEach(c -> {

                    if (!client.getSessionId().equals(c.getSessionId()))
                    {
                        c.sendEvent("typing", data);
                    }

                });
            }
            typingStatusService.updateTying(client.getSessionId().toString());
        };
    }

    public DataListener<RoomRequestDTO> joinRoom() {
        return (client, data, ackSender) -> {
            boolean flag = chatRoomService.joinRoomHandle(client, data);
            if (ackSender.isAckRequested()) {
                ackSender.sendAckData(flag ? "ok" : "failed");
            }
        };
    }

    public DataListener<RoomRequestDTO> leaveRoom() {
        return (client, data, ackSender) -> {
            boolean flag = chatRoomService.leaveRoomHandle(client, data);
            if (ackSender.isAckRequested()) {
                ackSender.sendAckData(flag ? "ok" : "failed");
            }
        };
    }

}
