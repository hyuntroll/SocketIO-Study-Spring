package com.example.socketiostudyspring.service;

import com.example.socketiostudyspring.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final Map<String, Room> roomMap = new ConcurrentHashMap<>();

    public boolean isRoom(String roomId) {
        return this.roomMap.containsKey(roomId);
    }

    public Room getRoom(String roomId) {
        return isRoom(roomId) ? this.roomMap.get(roomId) : null;
    }

    public boolean createRoom(String roomId, String password) {
        if (isRoom(roomId)) { return false; }

        Room room = new Room(roomId, password);
        this.roomMap.put(roomId, room);

        return true;
    }

    public boolean deleteRoom(String roomId, String password) {
        if (!isRoom(roomId)) { return false; }

        Room room = getRoom(roomId);
        if (!room.getPassword().equals(password)) { return false;}
        this.roomMap.remove(roomId);

        return true;
    }



}
