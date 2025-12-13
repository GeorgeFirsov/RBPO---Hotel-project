package com.example.service;

import com.example.model.Guest;
import com.example.storage.DB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final DB db;
    public GuestService(DB db) { this.db = db; }
    public Guest addGuest(int guestId, String guestName) { return db.saveGuest(guestId, guestName); }
    public List<Guest> getAllGuests() { return db.guests(); }
    public Object updateGuestName(int guestId, String guestName) {
        Guest g = db.findGuest(guestId);
        if (g == null) return "guest not found";
        g.setGuestName(guestName);
        return db.saveGuest(g.getGuestId(), g.getGuestName());
    }
    public void deleteGuest(int guestId) { db.deleteGuest(guestId); }
}
