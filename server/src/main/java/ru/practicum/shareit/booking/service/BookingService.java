package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(Booking bookingRequestDto, int bookerId, int itemId);

    BookingResponseDto approveBooking(boolean isApproved, int bookingId, int itemOwnerId);

    BookingResponseDto getBookingById(int bookingId, int userId);

    List<BookingResponseDto> getBookingsForCurrentUser(int bookerId, String bookingStatus);

    List<BookingResponseDto> getBookingsByItemOwnerId(int userId, String bookingStatus);
}