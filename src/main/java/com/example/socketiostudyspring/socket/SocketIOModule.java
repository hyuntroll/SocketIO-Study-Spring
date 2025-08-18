package com.example.socketiostudyspring.socket;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.socketiostudyspring.model.*;
import com.example.socketiostudyspring.service.ChatRoomService;
import com.example.socketiostudyspring.service.TypingStatusService;
import com.example.socketiostudyspring.service.UserStatusService;
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

    public SocketIOModule(SocketIOServer server, TypingStatusService typingStatusService, ChatRoomService chatRoomService, UserStatusService userStatusService) {
        this.server = server;
        this.typingStatusService = typingStatusService;
        this.chatRoomService = chatRoomService;
        this.userStatusService = userStatusService;

        // add Handler
        // namespace - "/"
        server.addConnectListener(listenConnected("/"));
        server.addDisconnectListener(listenDisconnected("/"));

        server.addEventListener("typing", Typing.class, receiveTyping());
        server.addEventListener("message", Message.class, chatReceiver());
        server.addEventListener("join-room", RoomRequestDTO.class, joinRoom());
        server.addEventListener("leave-room", RoomRequestDTO.class, leaveRoom());

        // namespace - "/room"
        server.addNamespace("/room").addConnectListener(listenConnected("/room"));
        server.addNamespace("/room").addDisconnectListener(listenDisconnected("/room"));

        server.addNamespace("/room").addDisconnectListener(listenDisconnectedRooms());
    }

    /**
     * 클라이언트 연결 리스너
     */
    public ConnectListener listenConnected(String namespace) {
        return client -> userStatusService.notifyConnection(client, server, namespace);
    }

    /**
     * 클라이언트 연결 해제 리스너
     */
    public DisconnectListener listenDisconnected(String namespace) {
        return client -> userStatusService.notifyDisconnection(client, server, namespace);
    }

    public DisconnectListener listenDisconnectedFromRoom() {
        return client -> {
          userStatusService.notifyDisconnection(client, server, "/room");
          chatRoomService.handleDisconnectFromRooms(client.getSessionId().toString());
        };
    }

    public DataListener<Message> chatReceiver() {
        return (client, data, ackSender) -> {
            log.info("chat received: {}", data.getMessage());

            String roomId = data.getRoomId(); //TODO: Optional 추가 필요

            typingStatusService.removeTyping(client.getSessionId().toString());

            server.getAllClients().forEach(c -> {
                if (!client.getSessionId().equals(c.getSessionId())) {
                    if (roomId == null) {
//                        System.out.println(c.getSessionId() + ": " + data.getMessage());
                        c.sendEvent("message", data);
                    }
                    else {
                        if (c.getAllRooms().contains(roomId) ) {
                            c.sendEvent("room-message", data);
                        }
                    }

                }
            });

            if ( ackSender.isAckRequested() ) { // 클라이언트에서 AckRequest요청을 한다면
                ackSender.sendAckData("ok");
            }

        };
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
            if (client.getAllRooms().contains(data.getRoomId())) { return ; }

            if (!chatRoomService.isRoom(data.getRoomId())) {
                chatRoomService.createRoom(data.getRoomId(), data.getPassword());
            }
//            log.info("{}, {}", data.getRoomId(), data.getPassword());
            if (chatRoomService.joinRoom(data.getRoomId(), data.getPassword(), client.getSessionId().toString())) {
                client.joinRoom(data.getRoomId());
                if (ackSender.isAckRequested()) { ackSender.sendAckData("ok"); }
            }
            else
                log.info("room join failed: {}", data.getRoomId());
            if (ackSender.isAckRequested()) { ackSender.sendAckData("failed"); }


        };
    }

    public DataListener<RoomRequestDTO> leaveRoom() {
        return (client, data, ackSender) -> {
            if ( !client.getAllRooms().contains(data.getRoomId())) { return ;}

            if (!chatRoomService.isRoom(data.getRoomId())) {
                return;
            }

            if (chatRoomService.leaveRoom(data.getRoomId(), client.getSessionId().toString())) {
                client.leaveRoom(data.getRoomId());
                if (ackSender.isAckRequested()) { ackSender.sendAckData("ok"); }
            }
            else {
                log.info("leave room failed: {}", data.getRoomId());
                if (ackSender.isAckRequested()) {
                    ackSender.sendAckData("failed");
                }
            }
        };
    }

}
