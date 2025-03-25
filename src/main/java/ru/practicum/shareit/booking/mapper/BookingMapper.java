package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDto toDtoModel(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    public static Booking toRegularModel(Integer bookingId, BookingDto bookingDto, User booker, Item itemToBooking, BookingStatus bookingStatus) {
        return new Booking(
                bookingId,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                itemToBooking,
                booker,
                bookingStatus
        );
    }
}
