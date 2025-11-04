package com.example.controller;

import com.example.model.Room;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final DB db;
    public RoomController(DB db){ this.db = db; }

    @PostMapping("/add")
    public Room add(@RequestParam int hotelId, @RequestParam int roomId){
        return db.saveRoom(hotelId, roomId);
    }

    @GetMapping
    public List<Room> all(){ return db.rooms(); }

    @PutMapping("/update")
    public Object update(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int newRoomId){
        return db.renameRoom(hotelId, roomId, newRoomId);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int hotelId, @RequestParam int roomId){
        db.deleteRoom(hotelId, roomId);
    }
}
