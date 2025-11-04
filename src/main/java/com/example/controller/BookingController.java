package com.example.controller;

import com.example.model.Booking;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final DB db;
    public BookingController(DB db){ this.db = db; }

    @PostMapping("/add")
    public Object add(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int guestId, @RequestParam String start, @RequestParam String end){
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        if (db.countOverlaps(hotelId, roomId, s, e, null) > 0) return "booking dates overlap";
        return db.addBooking(hotelId, roomId, guestId, s, e);
    }

    @GetMapping
    public List<Booking> all(){ return db.bookings(); }

    @PutMapping("/update")
    public Object update(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int guestId, @RequestParam String start, @RequestParam String end){
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        Booking target = db.findBooking(hotelId, roomId, guestId);
        if (target == null) return "booking not found";

        if (db.countOverlaps(hotelId, roomId, s, e, target.getId()) > 0) return "booking dates overlap";

        target.setStartDate(s);
        target.setEndDate(e);
        return db.saveBooking(target);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int guestId){
        return db.deleteBooking(hotelId, roomId, guestId) ? "deleted" : "booking not found";
    }
}
