package com.example.service;

import com.example.model.Booking;
import com.example.model.Room;
import com.example.storage.DB;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final DB db;
    public BookingService(DB db) { this.db = db; }

    public Object addBooking(int hotelId, int roomId, int guestId, String start, String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        if (db.countOverlaps(hotelId, roomId, s, e, null) > 0) return "booking dates overlap";
        return db.addBooking(hotelId, roomId, guestId, s, e);
    }

    public List<Booking> getAllBookings() {
        return db.bookings();
    }

    public Object updateBooking(int hotelId, int roomId, int guestId, String start, String end) {
        Booking target = db.findBooking(hotelId, roomId, guestId);
        if (target == null) return "booking not found";
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        if (db.countOverlaps(hotelId, roomId, s, e, target.getId()) > 0) return "booking dates overlap";
        target.setStartDate(s);
        target.setEndDate(e);
        return db.saveBooking(target);
    }

    public String deleteBooking(int hotelId, int roomId, int guestId) {
        return db.deleteBooking(hotelId, roomId, guestId) ? "deleted" : "booking not found";
    }

    public List<Booking> bookingsByGuest(int guestId) {
        List<Booking> res = new ArrayList<>();
        for (Booking b : db.bookings()) if (b.getGuestId() == guestId) res.add(b);
        return res;
    }

    public List<Room> availableRooms(int hotelId, String start, String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        List<Integer> busy = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {
                boolean overlap = !(b.getEndDate().isBefore(s) || b.getStartDate().isAfter(e));
                if (overlap && !busy.contains(b.getRoomId())) busy.add(b.getRoomId());
            }
        }
        List<Room> res = new ArrayList<>();
        for (Room r : db.rooms()) {
            if (r.getHotelId() == hotelId && !busy.contains(r.getRoomId())) res.add(r);
        }
        return res;
    }

    public int occupancy(int hotelId, String date) {
        LocalDate d = LocalDate.parse(date);
        List<Integer> used = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {
                boolean inside = !d.isBefore(b.getStartDate()) && !d.isAfter(b.getEndDate());
                if (inside && !used.contains(b.getRoomId())) used.add(b.getRoomId());
            }
        }
        return used.size();
    }

    public List<Room> occupiedRooms(int hotelId, String date) {
        LocalDate d = LocalDate.parse(date);
        List<Integer> used = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {
                boolean inside = !d.isBefore(b.getStartDate()) && !d.isAfter(b.getEndDate());
                if (inside && !used.contains(b.getRoomId())) used.add(b.getRoomId());
            }
        }
        List<Room> res = new ArrayList<>();
        for (Room r : db.rooms()) {
            if (r.getHotelId() == hotelId && used.contains(r.getRoomId())) res.add(r);
        }
        return res;
    }

    public List<Booking> hotelRange(int hotelId, String start, String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        List<Booking> res = new ArrayList<>();
        for (Booking b : db.bookings()) {
            if (b.getHotelId() == hotelId) {
                boolean overlap = !(b.getEndDate().isBefore(s) || b.getStartDate().isAfter(e));
                if (overlap) res.add(b);
            }
        }
        return res;
    }
}
