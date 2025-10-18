package com.example.controller;

import com.example.model.Guest;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @PostMapping("/add")
    public Guest add(@RequestParam("guestId") int guestId,
                     @RequestParam("guestName") String guestName) {
        Guest g = new Guest(guestId, guestName);
        DB.guests.add(g);
        return g;
    }

    @GetMapping
    public List<Guest> all() {
        return DB.guests;
    }

    @PutMapping("/update")
    public Object update(@RequestParam("guestId") int guestId,
                         @RequestParam("guestName") String guestName) {
        for (Guest g : DB.guests) {
            if (g.getGuestId() == guestId) {
                g.setGuestName(guestName);
                return g;
            }
        }
        return "guest not found";
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("guestId") int guestId) {
        DB.guests.removeIf(g -> g.getGuestId() == guestId);
        DB.bookings.removeIf(b -> b.getGuestId() == guestId);
    }
}
