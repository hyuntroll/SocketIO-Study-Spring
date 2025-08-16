package com.example.socketiostudyspring.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {

    private String message;
    private String user;
    private String roomId;

    public Message(String message, String user) {
        this.message = message;
        this.user = user;
    }
    public Message(String message, String user, String roomId) {
        this.message = message;
        this.user = user;
        this.roomId = roomId;
    }

}
