package com.example.controller;

import com.example.model.Room;
import com.example.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    public RoomController(RoomService roomService) { this.roomService = roomService; }

    @PostMapping("/add")
    public Room add(@RequestParam int hotelId, @RequestParam int roomId) {
        return roomService.addRoom(hotelId, roomId);
    }

    @GetMapping
    public List<Room> all() {
        return roomService.getAllRooms();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int newRoomId) {
        return roomService.renameRoom(hotelId, roomId, newRoomId);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int hotelId, @RequestParam int roomId) {
        roomService.deleteRoom(hotelId, roomId);
    }
}
