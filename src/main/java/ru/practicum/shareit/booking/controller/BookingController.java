package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable int bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping
    public List<Booking> getBookingsByCurrentUser(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookingsForCurrentUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookingsByItemOwnerId(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public Booking changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam boolean approved) {
        return bookingService.approveBooking(approved, bookingId);
    }

    @PostMapping
    public Booking addNewBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, BookingDto bookingDto){

        return bookingService.createBooking(bookingDto);
    }

}
