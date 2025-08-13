package com.example.socketiostudyspring.socket;


import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class SocketIOLifeCycle {

    private final SocketIOServer server;

    public SocketIOLifeCycle(SocketIOServer server) {
        this.server = server;
    }

    @PostConstruct
    public void start() {
        server.start();
        System.out.println("server is running...");
    }


    @PreDestroy
    public void stop() {
        server.stop();
        System.out.println("server is ended");
    }

}
