package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable int bookingId) {
        log.info("Пришел GET запрос на /bookings/{} от пользователя с id {}", bookingId, userId);
        ResponseEntity<Object> booking = bookingClient.getBookingById(userId, bookingId);
        log.info("Отправлен ответ на GET запрос /bookings/{} с телом: {}", bookingId, booking);
        return booking;
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByCurrentUser(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Пришел GET запрос на /bookings?state={} от пользователя с id {}", state, userId);
        ResponseEntity<Object> bookings = bookingClient.getBookingByCurrentUser(userId, state);
        log.info("Отправлен ответ на GET запрос /bookings?state={} с телом: {}", state, bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Пришел GET запрос на /bookings/owner?state={} от пользователя с id {}", state, userId);
        ResponseEntity<Object> bookings = bookingClient.getBookingsByItemOwner(userId, state);
        log.info("Отправлен ответ на GET запрос /bookings/owner?state={} с телом: {}", state, bookings);
        return bookings;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam(name = "approved", required = true) Boolean approved) {
        log.info("Пришел PATCH запрос на /bookings/{}?approved={} от пользователя с id {}", bookingId, approved, userId);
        ResponseEntity<Object> booking = bookingClient.approveBooking(approved, bookingId, userId);
        log.info("Отправлен ответ на PATCH запрос /booking/{}?approved={} от пользователя с id {}", bookingId, approved, userId);
        return booking;
    }

    @PostMapping
    public ResponseEntity<Object> addNewBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Пришел POST запрос /bookings на добавление нового бронирования от пользователя с id {} с телом: {}", userId, bookingRequestDto);
        ResponseEntity<Object> booking = bookingClient.createBooking(userId, bookingRequestDto);
        log.info("Добавлено новое бронирование: {}", booking);
        return booking;
    }

}