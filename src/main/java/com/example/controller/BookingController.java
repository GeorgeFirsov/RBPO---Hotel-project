package com.example.controller;

import com.example.model.Booking;
import com.example.model.Room;
import com.example.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) { this.bookingService = bookingService; }

    @PostMapping("/add")
    public Object add(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int guestId, @RequestParam String start, @RequestParam String end) {
        return bookingService.addBooking(hotelId, roomId, guestId, start, end);
    }

    @GetMapping
    public List<Booking> all() {
        return bookingService.getAllBookings();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int guestId, @RequestParam String start, @RequestParam String end) {
        return bookingService.updateBooking(hotelId, roomId, guestId, start, end);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam int hotelId, @RequestParam int roomId, @RequestParam int guestId) {
        return bookingService.deleteBooking(hotelId, roomId, guestId);
    }

    @GetMapping("/by-guest")
    public List<Booking> byGuest(@RequestParam int guestId) {
        return bookingService.bookingsByGuest(guestId);
    }

    @GetMapping("/available")
    public List<Room> available(@RequestParam int hotelId, @RequestParam String start, @RequestParam String end) {
        return bookingService.availableRooms(hotelId, start, end);
    }

    @GetMapping("/occupancy")
    public int occupancy(@RequestParam int hotelId, @RequestParam String date) {
        return bookingService.occupancy(hotelId, date);
    }

    @GetMapping("/occupied-rooms")
    public List<Room> occupiedRooms(@RequestParam int hotelId, @RequestParam String date) {
        return bookingService.occupiedRooms(hotelId, date);
    }

    @GetMapping("/hotel-range")
    public List<Booking> hotelRange(@RequestParam int hotelId, @RequestParam String start, @RequestParam String end) {
        return bookingService.hotelRange(hotelId, start, end);
    }
}
