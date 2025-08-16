package com.example.socketiostudyspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    private String roomId;

    private String password;

}
