package com.example.socketiostudyspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Room {

    private String roomId;
    private String password;
    private int sessionCount;
    private final Set<Session> sessionSet;

    public Room(String roomId, String password) {
        this.roomId = roomId;
        this.password = password;
        this.sessionCount = 0;
        this.sessionSet = ConcurrentHashMap.newKeySet();
    }
}
