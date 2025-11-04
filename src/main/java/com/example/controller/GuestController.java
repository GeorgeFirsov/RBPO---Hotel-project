package com.example.controller;

import com.example.model.Guest;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final DB db;
    public GuestController(DB db){ this.db = db; }

    @PostMapping("/add")
    public Guest add(@RequestParam int guestId, @RequestParam String guestName){
        return db.saveGuest(guestId, guestName);
    }

    @GetMapping
    public List<Guest> all(){ return db.guests(); }

    @PutMapping("/update")
    public Object update(@RequestParam int guestId, @RequestParam String guestName){
        Guest g = db.findGuest(guestId);
        if (g == null) return "guest not found";
        g.setGuestName(guestName);
        return db.saveGuest(g.getGuestId(), g.getGuestName());
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int guestId){ db.deleteGuest(guestId); }
}
