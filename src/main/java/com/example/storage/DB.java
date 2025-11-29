package com.example.storage;

import com.example.model.Booking;
import com.example.model.Guest;
import com.example.model.Hotel;
import com.example.model.Room;
import com.example.model.Room.RoomKey;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class DB {
    @PersistenceContext
    private EntityManager em;

    public List<Hotel> hotels() {
        return em.createQuery("from Hotel", Hotel.class).getResultList();
    }

    public Hotel saveHotel(int hotelId, String hotelName, String address) {
        Hotel h = em.find(Hotel.class, hotelId);
        if (h == null) h = new Hotel(hotelId, hotelName, address);
        else {
            h.setHotelName(hotelName);
            h.setAddress(address);
        }
        return em.merge(h);
    }

    public void deleteHotel(int id) {
        Hotel h = em.find(Hotel.class, id);
        if (h != null) em.remove(h);
    }

    public Object updateHotelName(int hotelId, String hotelName) {
        Hotel h = em.find(Hotel.class, hotelId);
        if (h == null) return "hotel not found";
        h.setHotelName(hotelName);
        return em.merge(h);
    }

    public List<Guest> guests() {
        return em.createQuery("from Guest", Guest.class).getResultList();
    }

    public Guest saveGuest(int guestId, String guestName) {
        Guest g = em.find(Guest.class, guestId);
        if (g == null) g = new Guest(guestId, guestName);
        else g.setGuestName(guestName);
        return em.merge(g);
    }

    public Guest findGuest(int guestId) {
        return em.find(Guest.class, guestId);
    }

    public void deleteGuest(int guestId) {
        Guest g = em.find(Guest.class, guestId);
        if (g != null) em.remove(g);
    }

    public List<Room> rooms() {
        return em.createQuery("from Room", Room.class).getResultList();
    }

    public Room saveRoom(int hotelId, int roomId) {
        RoomKey key = new RoomKey(hotelId, roomId);
        Room r = em.find(Room.class, key);
        if (r == null) r = new Room(hotelId, roomId);
        return em.merge(r);
    }

    public Object renameRoom(int hotelId, int roomId, int newRoomId) {
        RoomKey oldKey = new RoomKey(hotelId, roomId);
        RoomKey newKey = new RoomKey(hotelId, newRoomId);
        Room existing = em.find(Room.class, oldKey);
        if (existing == null) return "room not found";
        Room conflict = em.find(Room.class, newKey);
        if (conflict != null) return "room already exists";
        em.createQuery("update Booking b set b.roomId=:newRoomId where b.hotelId=:hotelId and b.roomId=:roomId").setParameter("newRoomId", newRoomId).setParameter("hotelId", hotelId).setParameter("roomId", roomId).executeUpdate();
        em.remove(existing);
        Room newRoom = new Room(hotelId, newRoomId);
        em.persist(newRoom);
        return "ok";
    }

    public void deleteRoom(int hotelId, int roomId) {
        RoomKey key = new RoomKey(hotelId, roomId);
        Room r = em.find(Room.class, key);
        if (r != null) em.remove(r);
    }

    public List<Booking> bookings() {
        return em.createQuery("from Booking", Booking.class).getResultList();
    }

    public Booking addBooking(int hotelId, int roomId, int guestId, LocalDate start, LocalDate end) {
        Booking b = new Booking(null, hotelId, roomId, guestId, start, end);
        em.persist(b);
        return b;
    }

    public long countOverlaps(int hotelId, int roomId, LocalDate start, LocalDate end, Long exceptId) {
        String jpql = "select count(b) from Booking b where b.hotelId=:hotelId and b.roomId=:roomId and not (b.endDate < :start or b.startDate > :end)";
        if (exceptId != null) jpql += " and b.id <> :exceptId";
        TypedQuery<Long> q = em.createQuery(jpql, Long.class).setParameter("hotelId", hotelId).setParameter("roomId", roomId).setParameter("start", start).setParameter("end", end);
        if (exceptId != null) q.setParameter("exceptId", exceptId);
        return q.getSingleResult();
    }

    public Booking findBooking(int hotelId, int roomId, int guestId) {
        List<Booking> list = em.createQuery("from Booking b where b.hotelId=:hotelId and b.roomId=:roomId and b.guestId=:guestId", Booking.class).setParameter("hotelId", hotelId).setParameter("roomId", roomId).setParameter("guestId", guestId).setMaxResults(1).getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public Booking saveBooking(Booking b) {
        return em.merge(b);
    }

    public boolean deleteBooking(int hotelId, int roomId, int guestId) {
        Booking b = findBooking(hotelId, roomId, guestId);
        if (b == null) return false;
        em.remove(b);
        return true;
    }
}
