package com.example.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${socketio.server.hostname}")
    private String hostname;

    @Value("${socketio.server.port")
    private int port;

    /**
     * Tomcat 서버와 별도로 돌아가는 netty 서버 생성
     * */

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        return new SocketIOServer(config);
    }
}
