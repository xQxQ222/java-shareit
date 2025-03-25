package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingDbService implements BookingService {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto) {
        User booker = userRepository.findById(bookingDto.getBookerId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в БД"));
        Item itemToBook = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден в БД"));
        Booking booking = BookingMapper.toRegularModel(null, bookingDto,
                booker, itemToBook);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(boolean isApproved, int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена в БД"));
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);

    }

    @Override
    public Booking getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена в БД"));
    }

    @Override
    public List<Booking> getBookingsForCurrentUser(int bookerId, String bookingStatus) {
        BookingStatus status;
        switch (bookingStatus){
            case "ALL":
                break;
        }
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByItemOwnerId(int userId, String bookingStatus) {
        return List.of();
    }
}
