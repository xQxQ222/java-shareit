package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, int bookerId);

    Booking approveBooking(boolean isApproved, int bookingId, int itemOwnerId);

    Booking getBookingById(int bookingId, int userId);

    List<Booking> getBookingsForCurrentUser(int bookerId, String bookingStatus);

    List<Booking> getBookingsByItemOwnerId(int userId, String bookingStatus);
}