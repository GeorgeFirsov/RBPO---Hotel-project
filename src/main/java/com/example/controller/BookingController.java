package com.example.controller;

import com.example.model.Room;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @PostMapping("/add")
    public Room add(@RequestParam("hotelId") int hotelId,
                    @RequestParam("roomId") int roomId) {
        Room r = new Room(hotelId, roomId);
        DB.rooms.add(r);
        return r;
    }

    @GetMapping
    public List<Room> all() {
        return DB.rooms;
    }

    @PutMapping("/update")
    public Object update(@RequestParam("hotelId") int hotelId,
                         @RequestParam("roomId") int roomId,
                         @RequestParam("newRoomId") int newRoomId) {
        Room target = null;
        for (Room r : DB.rooms) {
            if (r.getHotelId() == hotelId && r.getRoomId() == roomId) {
                target = r; break;
            }
        }
        if (target == null) return "room not found";

        target.setRoomId(newRoomId);
        for (var b : DB.bookings) {
            if (b.getHotelId() == hotelId && b.getRoomId() == roomId) {
                b.setRoomId(newRoomId);
            }
        }
        return target;
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("hotelId") int hotelId,
                       @RequestParam("roomId") int roomId) {
        DB.rooms.removeIf(r -> r.getHotelId() == hotelId && r.getRoomId() == roomId);
        DB.bookings.removeIf(b -> b.getHotelId() == hotelId && b.getRoomId() == roomId);
    }
}
