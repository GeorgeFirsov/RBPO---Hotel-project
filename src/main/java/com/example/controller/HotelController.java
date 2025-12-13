package com.example.controller;

import com.example.model.Hotel;
import com.example.service.HotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;
    public HotelController(HotelService hotelService) { this.hotelService = hotelService; }

    @PostMapping("/add")
    public Hotel add(@RequestParam int hotelId, @RequestParam String hotelName, @RequestParam String address) {
        return hotelService.addHotel(hotelId, hotelName, address);
    }

    @GetMapping
    public List<Hotel> all() {
        return hotelService.getAllHotels();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int hotelId, @RequestParam String hotelName) {
        return hotelService.updateHotelName(hotelId, hotelName);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int hotelId) {
        hotelService.deleteHotel(hotelId);
    }
}
