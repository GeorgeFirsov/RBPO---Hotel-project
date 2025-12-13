package com.example.controller;

import com.example.model.Booking;
import com.example.model.Room;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @GetMapping("/available")
    public List<Room> available(@RequestParam int hotelId, @RequestParam String start, @RequestParam String end){
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        List<Integer> busy = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {boolean overlap = !s.isAfter(b.getEndDate()) && !e.isBefore(b.getStartDate());if (overlap && !busy.contains(b.getRoomId())) busy.add(b.getRoomId());
            }
        }

        List<Room> res = new ArrayList<>();
        for (Room r : db.rooms()) {
            if (r.getHotelId() == hotelId && !busy.contains(r.getRoomId())) res.add(r);
        }
        return res;
    }

    @GetMapping("/occupancy")
    public int occupancy(@RequestParam int hotelId, @RequestParam String date){
        LocalDate d = LocalDate.parse(date);
        List<Integer> used = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {boolean inside = !d.isBefore(b.getStartDate()) && !d.isAfter(b.getEndDate());if (inside && !used.contains(b.getRoomId())) used.add(b.getRoomId());
            }
        }
        return used.size();
    }

    @GetMapping("/by-guest")
    public List<Booking> byGuest(@RequestParam int guestId){
        List<Booking> res = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getGuestId() == guestId) res.add(b);
        }
        return res;
    }

    @GetMapping("/occupied-rooms")
    public List<Room> occupiedRooms(@RequestParam int hotelId, @RequestParam String date){
        LocalDate d = LocalDate.parse(date);
        List<Integer> used = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {boolean inside = !d.isBefore(b.getStartDate()) && !d.isAfter(b.getEndDate());if (inside && !used.contains(b.getRoomId())) used.add(b.getRoomId());
            }
        }
        List<Room> res = new ArrayList<>();
        for (Room r : db.rooms()) {
            if (r.getHotelId() == hotelId && used.contains(r.getRoomId())) res.add(r);
        }
        return res;
    }

    @GetMapping("/hotel-range")
    public List<Booking> hotelRange(@RequestParam int hotelId, @RequestParam String start, @RequestParam String end){
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        List<Booking> res = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {boolean overlap = !s.isAfter(b.getEndDate()) && !e.isBefore(b.getStartDate());if (overlap) res.add(b);
            }
        }
        return res;
    }
}
