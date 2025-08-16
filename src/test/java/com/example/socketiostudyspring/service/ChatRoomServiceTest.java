package com.example.socketiostudyspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService service;

    @Test
    public void createRoom() {

        System.out.println( service.createRoom("test1", "abcd" ));
        System.out.println( service.createRoom("test1", "abcd" ));
        System.out.println( service.isRoom("test1" ));
    }

    @Test
    public void deleteRoom() {
        System.out.println( service.createRoom("test1", "abcd" ));
        System.out.println( service.isRoom("test1" ));
        System.out.println( service.deleteRoom("test1", "123" ));
        System.out.println( service.deleteRoom("test1", "abcd" ));
        System.out.println( service.isRoom("test1" ));

    }

    @Test
    public void joinRoom() {
        System.out.println( service.createRoom("test1", "abcd" ));
        System.out.println( service.joinRoom("test1", "abcd", "k1"));
        System.out.println( service.joinRoom("test1", "abcd", "k1"));
        System.out.println( service.joinRoom("test1", "abcd", "k2"));
        System.out.println( service.joinRoom("test1", "abcd", "k3"));
        System.out.println( service.getSessionCount("test1"));
    }

    @Test
    public void leaveRoom() {
        System.out.println( service.createRoom("test1", "abcd" ));
        System.out.println( service.joinRoom("test1", "abcd", "k1"));
        System.out.println( service.isRoom("test1"));

        System.out.println( service.leaveRoom("test1", "abcd", "k1"));
        System.out.println( service.isRoom("test1"));

    }

}