package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "rooms")
@IdClass(Room.RoomKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private int hotelId;
    @Id
    private int roomId;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomKey implements Serializable {
        private int hotelId;
        private int roomId;
    }
}
