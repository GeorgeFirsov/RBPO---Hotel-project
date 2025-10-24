package com.example.controller;

import com.example.model.Booking;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @PostMapping("/add")
    public Object add(@RequestParam("hotelId") int hotelId, @RequestParam("roomId") int roomId, @RequestParam("guestId") int guestId, @RequestParam("start") String start, @RequestParam("end") String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        for (Booking b : DB.bookings) {
            if (b.getHotelId() == hotelId && b.getRoomId() == roomId) {
                if (!s.isAfter(b.getEndDate()) && !e.isBefore(b.getStartDate())) {
                    return "booking dates overlap";
                }
            }
        }
        Booking nb = new Booking(hotelId, roomId, guestId, s, e);
        DB.bookings.add(nb);
        return nb;
    }

    @GetMapping
    public List<Booking> all() {
        return DB.bookings;
    }

    @PutMapping("/update")
    public Object update(@RequestParam("hotelId") int hotelId, @RequestParam("roomId") int roomId, @RequestParam("guestId") int guestId, @RequestParam("start") String start, @RequestParam("end") String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        Booking target = null;
        for (Booking b : DB.bookings) {
            if (b.getHotelId() == hotelId && b.getRoomId() == roomId && b.getGuestId() == guestId) {
                target = b; break;
            }
        }
        if (target == null) return "booking not found";

        for (Booking b : DB.bookings) {
            if (b == target) continue;
            if (b.getHotelId() == hotelId && b.getRoomId() == roomId) {
                if (!s.isAfter(b.getEndDate()) && !e.isBefore(b.getStartDate())) {
                    return "booking dates overlap";
                }
            }
        }
        target.setStartDate(s);
        target.setEndDate(e);
        return target;
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("hotelId") int hotelId, @RequestParam("roomId") int roomId, @RequestParam("guestId") int guestId) {
        boolean removed = DB.bookings.removeIf(b -> b.getHotelId() == hotelId && b.getRoomId() == roomId && b.getGuestId() == guestId);
        return removed ? "deleted" : "booking not found";
    }
}
