package com.example.socketiostudyspring.model;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoomRequestDTO {

    private String roomId;

    private String password;
}
