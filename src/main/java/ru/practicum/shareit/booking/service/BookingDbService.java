package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.ItemNotAvailable;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingDbService implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto, int bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в БД"));
        Item itemToBook = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден в БД"));
        if (!itemToBook.isAvailable()) {
            throw new ItemNotAvailable("Данный предмет не доступен для брони");
        }
        Booking booking = BookingMapper.toRegularModel(0, bookingDto,
                booker, itemToBook, BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(boolean isApproved, int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена в БД"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotOwnerException("Статус брони может изменять только владелец вещи");
        }
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);

    }

    @Override
    public Booking getBookingById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена в БД"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotOwnerException("Информацию о бронировании могут просматривать только владелец вещи, либо создатель брони");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsForCurrentUser(int bookerId, String bookingStatus) {
        List<Booking> bookings = switch (bookingStatus) {
            case "ALL" -> bookingRepository.findByBookerIdOrderByStartAsc(bookerId);
            case "WAITING" ->
                    bookingRepository.findByBookerIdAndStatusContainingOrderByStartAsc(bookerId, BookingStatus.WAITING);
            case "REJECTED" ->
                    bookingRepository.findByBookerIdAndStatusContainingOrderByStartAsc(bookerId, BookingStatus.REJECTED);
            case "FUTURE" ->
                    bookingRepository.findByBookerIdAndStartAfterOrderByStartAsc(bookerId, LocalDateTime.now());
            case "PAST" -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartAsc(bookerId, LocalDateTime.now());
            case "CURRENT" ->
                    bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(bookerId, LocalDateTime.now(), LocalDateTime.now());
            default -> throw new ValidationException("Неправильно указан статус");
        };
        if (bookings.isEmpty()) {
            throw new NotFoundException("Данный пользователь не делал бронирований");
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByItemOwnerId(int userId, String bookingStatus) {
        List<Booking> bookings = List.of();
        switch (bookingStatus) {
            case "ALL" -> bookings = bookingRepository.findByItemOwnerIdAll(userId);
            case "WAITING" -> bookings = bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING);
            case "REJECTED" -> bookings = bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED);
            case "FUTURE" -> bookings = bookingRepository.findByItemOwnerIdFuture(userId, LocalDateTime.now());
            case "PAST" -> bookings = bookingRepository.findByItemOwnerIdPast(userId, LocalDateTime.now());
            case "CURRENT" ->
                    bookings = bookingRepository.findByItemOwnerIdCurrent(userId, LocalDateTime.now(), LocalDateTime.now());
            default -> throw new ValidationException("Неправильно указан статус");
        }
        if (bookings.isEmpty()) {
            throw new NotFoundException("Для данного пользователя не найдены брони на его предмет");
        }
        return bookings;
    }
}
