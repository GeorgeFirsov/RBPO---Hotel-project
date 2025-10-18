package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Booking {
    private int hotelId;
    private int roomId;
    private int guestId;
    private LocalDate startDate;
    private LocalDate endDate;
}
