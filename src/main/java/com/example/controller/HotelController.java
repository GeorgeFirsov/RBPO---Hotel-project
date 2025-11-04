package com.example.controller;

import com.example.model.Hotel;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final DB db;
    public HotelController(DB db){ this.db = db; }

    @PostMapping("/add")
    public Hotel add(@RequestParam int hotelId, @RequestParam String hotelName, @RequestParam String address){
        return db.saveHotel(hotelId, hotelName, address);
    }

    @GetMapping
    public List<Hotel> all(){ return db.hotels(); }

    @PutMapping("/update")
    public Object update(@RequestParam int hotelId, @RequestParam String hotelName){
        return db.updateHotelName(hotelId, hotelName);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int hotelId){ db.deleteHotel(hotelId); }
}
