package com.example.service;

import com.example.model.Room;
import com.example.storage.DB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final DB db;
    public RoomService(DB db) { this.db = db; }
    public Room addRoom(int hotelId, int roomId) { return db.saveRoom(hotelId, roomId); }
    public List<Room> getAllRooms() { return db.rooms(); }
    public Object renameRoom(int hotelId, int roomId, int newRoomId) { return db.renameRoom(hotelId, roomId, newRoomId); }
    public void deleteRoom(int hotelId, int roomId) { db.deleteRoom(hotelId, roomId); }
}
