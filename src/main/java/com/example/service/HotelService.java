package com.example.service;

import com.example.model.Hotel;
import com.example.storage.DB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    private final DB db;
    public HotelService(DB db) { this.db = db; }
    public Hotel addHotel(int hotelId, String hotelName, String address) { return db.saveHotel(hotelId, hotelName, address); }
    public List<Hotel> getAllHotels() { return db.hotels(); }
    public Object updateHotelName(int hotelId, String hotelName) { return db.updateHotelName(hotelId, hotelName); }
    public void deleteHotel(int hotelId) { db.deleteHotel(hotelId); }
}
