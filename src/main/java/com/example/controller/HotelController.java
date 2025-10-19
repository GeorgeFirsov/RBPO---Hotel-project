package com.example.controller;

import com.example.model.Hotel;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @PostMapping("/add")
    public Hotel add(@RequestParam("hotelId") int hotelId,
                     @RequestParam("hotelName") String hotelName,
                     @RequestParam("address") String address) {
        Hotel h = new Hotel(hotelId, hotelName, address);
        DB.hotels.add(h);
        return h;
    }

    @GetMapping
    public List<Hotel> all() {
        return DB.hotels;
    }

    @PutMapping("/update")
    public Object update(@RequestParam("hotelId") int hotelId,
                         @RequestParam("hotelName") String hotelName) {
        for (Hotel h : DB.hotels) {
            if (h.getHotelId() == hotelId) {
                h.setHotelName(hotelName);
                return h;
            }
        }
        return "hotel not found";
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("hotelId") int hotelId) {
        DB.hotels.removeIf(h -> h.getHotelId() == hotelId);
        DB.rooms.removeIf(r -> r.getHotelId() == hotelId);
        DB.bookings.removeIf(b -> b.getHotelId() == hotelId);
    }
}
