package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto);

    Booking approveBooking(boolean isApproved, int bookingId);

    Booking getBookingById(int bookingId);

    List<Booking> getBookingsForCurrentUser(int bookerId, String bookingStatus);

    List<Booking> getBookingsByItemOwnerId(int userId, String bookingStatus);

}
