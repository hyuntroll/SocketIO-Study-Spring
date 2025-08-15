package com.example.socketiostudyspring.service;

import com.example.socketiostudyspring.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
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

    public boolean isCorrect(String roomId, String password) {
        if (!isRoom(roomId)) { return false; }

        return getRoom(roomId).getPassword().equals(password);
    }

    public boolean createRoom(String roomId, String password) {
        if (isRoom(roomId)) { return false; }

        Room room = new Room(roomId, password);
        this.roomMap.put(roomId, room);

        return true;
    }

    public boolean deleteRoom(String roomId, String password) {
        if (!isRoom(roomId) || !isCorrect(roomId, password)) { return false; }

        this.roomMap.remove(roomId);
        return true;
    }

    public boolean joinRoom(String roomId, String password, String userId) {
        if (!isRoom(roomId) || !isCorrect(roomId, password)) { return false; }

        Room room = new Room(roomId, password);
        Set<String> roomSession = room.getSessionSet();
        if (roomSession.contains(userId)) { return false; }

        roomSession.add(userId);
        room.setSessionCount(room.getSessionCount()+1);
        return true;
    }

}
