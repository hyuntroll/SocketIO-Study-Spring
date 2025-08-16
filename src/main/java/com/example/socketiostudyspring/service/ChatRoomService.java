package com.example.socketiostudyspring.service;

import com.example.socketiostudyspring.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
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

    private Room getRoom(String roomId) {
        return isRoom(roomId) ? this.roomMap.get(roomId) : null;
    }

    public boolean isCorrect(String roomId, String password) {
        if (!isRoom(roomId)) { return true; }

        return !getRoom(roomId).getPassword().equals(password);
    }

    public boolean createRoom(String roomId, String password) {
        if (isRoom(roomId)) { return false; }

        log.info("room created: " + roomId);
        Room room = new Room(roomId, password);
        this.roomMap.put(roomId, room);

        return true;
    }

    public boolean deleteRoom(String roomId) {
        if (!isRoom(roomId)) { return false; }

        log.info("room deleted: " + roomId);
        this.roomMap.remove(roomId);
        return true;
    }

    public int getRoomCount() {
        return this.roomMap.size();
    }

    public int getSessionCount(String roomId) {
        if (!isRoom(roomId)) { return -1; }

        return getRoom(roomId).getSessionCount();

    }

    public boolean joinRoom(String roomId, String password, String userId) {
        if (!isRoom(roomId) || isCorrect(roomId, password)) { return false; }

        Room room = getRoom(roomId);
        Set<String> roomSession = room.getSessionSet();
        if (roomSession.contains(userId)) { return false; }

        log.info("room join session: " + userId);

        roomSession.add(userId);
        room.setSessionCount(room.getSessionCount()+1);
        return true;
    }

    public boolean leaveRoom(String roomId, String password, String userId) {
        if (!isRoom(roomId) || isCorrect(roomId, password)) { return false; }

        Room room = getRoom(roomId);
        Set<String> roomSession = room.getSessionSet();

        if (!roomSession.contains(userId)) { return false; }

        log.info("room leave session: " + userId);


        roomSession.remove(userId);
        room.setSessionCount(room.getSessionCount()-1);

        if (room.getSessionCount() <= 0 ) {
            deleteRoom(roomId);
        }

        return true;
    }

}
