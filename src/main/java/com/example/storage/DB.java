package com.example.storage;

import com.example.model.*;
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

    public Guest findGuest(int id){ return em.find(Guest.class, id); }

    public Guest saveGuest(int id, String name){
        Guest g = em.find(Guest.class, id);
        if (g == null) g = new Guest(id, name);
        else g.setGuestName(name);
        return em.merge(g);
    }

    public List<Guest> guests(){
        return em.createQuery("from Guest", Guest.class).getResultList();
    }

    public void deleteGuest(int id){
        em.createQuery("delete from Booking b where b.guestId=:gid").setParameter("gid", id).executeUpdate();
        Guest g = em.find(Guest.class, id);
        if (g != null) em.remove(g);
    }

    public List<Hotel> hotels(){
        return em.createQuery("from Hotel", Hotel.class).getResultList();
    }

    public Hotel saveHotel(int id, String name, String address){
        return em.merge(new Hotel(id, name, address));
    }

    public void deleteHotel(int id){
        em.createQuery("delete from Booking b where b.hotelId=:hid").setParameter("hid", id).executeUpdate();
        em.createQuery("delete from Room r where r.hotelId=:hid").setParameter("hid", id).executeUpdate();
        Hotel h = em.find(Hotel.class, id);
        if (h != null) em.remove(h);
    }

    public Object updateHotelName(int hotelId, String hotelName){
        Hotel h = em.find(Hotel.class, hotelId);
        if (h == null) return "hotel not found";
        h.setHotelName(hotelName);
        return em.merge(h);
    }

    public Room saveRoom(int hotelId, int roomId){
        Room existing = em.find(Room.class, new RoomKey(hotelId, roomId));
        if (existing != null) return existing;
        Room r = new Room(hotelId, roomId);
        em.persist(r);
        return r;
    }

    public List<Room> rooms(){
        return em.createQuery("from Room", Room.class).getResultList();
    }

    public void deleteRoom(int hotelId, int roomId){
        em.createQuery("delete from Booking b where b.hotelId=:hid and b.roomId=:rid").setParameter("hid", hotelId).setParameter("rid", roomId).executeUpdate();
        Room toDel = em.find(Room.class, new RoomKey(hotelId, roomId));
        if (toDel != null) em.remove(toDel);
    }

    public Object renameRoom(int hotelId, int roomId, int newRoomId){
        Room cur = em.find(Room.class, new RoomKey(hotelId, roomId));
        if (cur == null) return "room not found";
        Room clash = em.find(Room.class, new RoomKey(hotelId, newRoomId));
        if (clash != null) return "room already exists";

        em.createQuery("update Booking b set b.roomId=:nr where b.hotelId=:h and b.roomId=:r").setParameter("nr", newRoomId).setParameter("h", hotelId).setParameter("r", roomId).executeUpdate();

        em.remove(cur);
        em.persist(new Room(hotelId, newRoomId));
        return "ok";
    }

    public List<Booking> bookings(){
        return em.createQuery("from Booking", Booking.class).getResultList();
    }

    public long countOverlaps(int hotelId, int roomId, LocalDate s, LocalDate e, Long exceptId){
        String jpql = "select count(b) from Booking b " + "where b.hotelId=:h and b.roomId=:r " + "and :s <= b.endDate and :e >= b.startDate";
        if (exceptId != null) jpql += " and b.id <> :id";
        TypedQuery<Long> q = em.createQuery(jpql, Long.class).setParameter("h", hotelId).setParameter("r", roomId).setParameter("s", s).setParameter("e", e);
        if (exceptId != null) q.setParameter("id", exceptId);
        return q.getSingleResult();
    }

    public Booking addBooking(int hotelId, int roomId, int guestId, LocalDate s, LocalDate e){
        Booking b = new Booking(null, hotelId, roomId, guestId, s, e);
        em.persist(b);
        return b;
    }

    public Booking findBooking(int hotelId, int roomId, int guestId){
        List<Booking> list = em.createQuery("from Booking b where b.hotelId=:h and b.roomId=:r and b.guestId=:g", Booking.class).setParameter("h", hotelId).setParameter("r", roomId).setParameter("g", guestId).setMaxResults(1).getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public Booking saveBooking(Booking b){ return em.merge(b); }

    public boolean deleteBooking(int hotelId, int roomId, int guestId){
        Booking b = findBooking(hotelId, roomId, guestId);
        if (b == null) return false;
        em.remove(b);
        return true;
    }
}
