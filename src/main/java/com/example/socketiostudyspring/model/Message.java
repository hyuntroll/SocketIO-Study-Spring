package com.example.socketiostudyspring.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
    private String message;
    private String user;
}
