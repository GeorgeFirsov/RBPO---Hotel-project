package com.example.controller;

import com.example.model.Guest;
import com.example.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService guestService;
    public GuestController(GuestService guestService) { this.guestService = guestService; }

    @PostMapping("/add")
    public Guest add(@RequestParam int guestId, @RequestParam String guestName) {
        return guestService.addGuest(guestId, guestName);
    }

    @GetMapping
    public List<Guest> all() {
        return guestService.getAllGuests();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int guestId, @RequestParam String guestName) {
        return guestService.updateGuestName(guestId, guestName);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int guestId) {
        guestService.deleteGuest(guestId);
    }
}
