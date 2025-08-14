package com.example.socketiostudyspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocketIoStudySpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketIoStudySpringApplication.class, args);
    }

}
