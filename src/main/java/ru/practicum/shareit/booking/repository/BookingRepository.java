package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookerIdAndStatusContainingOrderByStartAsc(int bookerId, BookingStatus status);

    List<Booking> findByBookerIdAndStartAfterOrderByStartAsc(int bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartAsc(int bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(int bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdOrderByStartAsc(int bookerId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.start < ?2")
    List<Booking> findWhereStartBefore(int itemId, LocalDateTime start);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.end > ?2")
    List<Booking> findWhereAfterEnd(int itemId, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = ?2")
    List<Booking> findByItemOwnerIdAndStatus(int ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start > ?2")
    List<Booking> findByItemOwnerIdFuture(int ownerId, LocalDateTime start);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.end < ?2")
    List<Booking> findByItemOwnerIdPast(int ownerId, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start > ?2 AND b.end < ?3")
    List<Booking> findByItemOwnerIdCurrent(int ownerId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1")
    List<Booking> findByItemOwnerIdAll(int ownerId);
}
