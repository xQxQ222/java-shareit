package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable int bookingId) {
        return bookingService.getBookingById(bookingId, userId);
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
        return bookingService.approveBooking(approved, bookingId, userId);
    }

    @PostMapping
    public Booking addNewBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Пришел POST запрос /bookings на добавление нового бронирования от пользователя с id {} с телом: {}", userId, bookingRequestDto);
        Booking newBooking = bookingService.createBooking(BookingMapper.toDomainModel(null, bookingRequestDto, null, null, BookingStatus.WAITING), userId, bookingRequestDto.getItemId());
        log.info("Добавлено новое бронирование: {}", newBooking);
        return newBooking;
    }

}