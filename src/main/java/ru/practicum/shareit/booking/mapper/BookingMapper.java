package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingRequestDto toDtoModel(Booking booking) {
        return new BookingRequestDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    public static Booking toDomainModel(Integer bookingId, BookingRequestDto bookingRequestDto, User booker, Item itemToBooking, BookingStatus bookingStatus) {
        return new Booking(
                bookingId,
                bookingRequestDto.getStart(),
                bookingRequestDto.getEnd(),
                itemToBooking,
                booker,
                bookingStatus
        );
    }
}
