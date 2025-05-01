package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
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
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto createBooking(Booking booking, int bookerId, int itemId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в БД"));
        booking.setBooker(booker);
        Item itemToBook = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден в БД"));
        if (!itemToBook.getAvailable()) {
            throw new ItemNotAvailable("Данный предмет не доступен для брони");
        }
        booking.setItem(itemToBook);

        return BookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(boolean isApproved, int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена в БД"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotOwnerException("Статус брони может изменять только владелец вещи");
        }
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toResponseDto(bookingRepository.save(booking));

    }

    @Override
    public BookingResponseDto getBookingById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена в БД"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotOwnerException("Информацию о бронировании могут просматривать только владелец вещи, либо создатель брони");
        }
        return BookingMapper.toResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsForCurrentUser(int bookerId, String bookingStatus) {
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
        return bookings.stream()
                .map(BookingMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByItemOwnerId(int userId, String bookingStatus) {
        List<Booking> bookings = List.of();
        switch (bookingStatus) {
            case "ALL" -> bookings = bookingRepository.findByItemOwnerIdAll(userId);
            case "WAITING" ->
                    bookings = bookingRepository.findByItemOwnerIdAll(userId).stream().filter(booking -> booking.getStatus() == BookingStatus.WAITING).toList();
            case "REJECTED" ->
                    bookings = bookingRepository.findByItemOwnerIdAll(userId).stream().filter(booking -> booking.getStatus() == BookingStatus.REJECTED).toList();
            case "FUTURE" ->
                    bookings = bookingRepository.findByItemOwnerIdAll(userId).stream().filter(booking -> LocalDateTime.now().plusHours(4).isBefore(booking.getStart())).toList();
            case "PAST" ->
                    bookings = bookingRepository.findByItemOwnerIdAll(userId).stream().filter(booking -> LocalDateTime.now().plusHours(4).isAfter(booking.getEnd())).toList();
            case "CURRENT" ->
                    bookings = bookingRepository.findByItemOwnerIdAll(userId).stream().filter(booking -> LocalDateTime.now().plusHours(4).isAfter(booking.getStart()) &&
                            LocalDateTime.now().plusHours(4).isBefore(booking.getEnd())).toList();
            default -> throw new ValidationException("Неправильно указан статус");
        }
        if (bookings.isEmpty()) {
            throw new NotFoundException("Для данного пользователя не найдены брони на его предмет");
        }
        return bookings.stream()
                .map(BookingMapper::toResponseDto)
                .toList();
    }
}
