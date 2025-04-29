package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable int bookingId) {
        log.info("Пришел GET запрос на /bookings/{}", bookingId);
        BookingResponseDto booking = bookingService.getBookingById(bookingId, userId);
        log.info("Отправлен ответ на GET /bookins/{} с телом: {}", bookingId, booking);
        return booking;
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByCurrentUser(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Пришел GET запрос на /bookings?state={}", state);
        List<BookingResponseDto> bookings = bookingService.getBookingsForCurrentUser(userId, state);
        log.info("Отправлен ответ на GET /bookings/state={} с телом: {}", state, bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Пришел GET запрос на /bookings/owner?state={}", state);
        List<BookingResponseDto> bookings = bookingService.getBookingsByItemOwnerId(userId, state);
        log.info("Отправлен ответ на GET /bookings/owner?state={} с телом: {}", state, bookings);
        return bookings;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam boolean approved) {
        log.info("Пришел PATCH запрос на /bookings/{} для смены статуса бронирования на: {}", bookingId, approved ? "Одобрено" : "Отказано");
        BookingResponseDto bookingResponseDto = bookingService.approveBooking(approved, bookingId, userId);
        log.info("Статус бронирования с id = {} поменян на: {}", bookingResponseDto.getId(), bookingResponseDto.getStatus());
        return bookingResponseDto;
    }

    @PostMapping
    public BookingResponseDto addNewBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Пришел POST запрос /bookings на добавление нового бронирования от пользователя с id {} с телом: {}", userId, bookingRequestDto);
        BookingResponseDto newBooking = bookingService.createBooking(BookingMapper.toDomainModel(null, bookingRequestDto, null, null, BookingStatus.WAITING), userId, bookingRequestDto.getItemId());
        log.info("Добавлено новое бронирование: {}", newBooking);
        return newBooking;
    }

}