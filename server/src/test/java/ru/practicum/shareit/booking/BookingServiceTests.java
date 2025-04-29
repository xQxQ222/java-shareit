package ru.practicum.shareit.booking;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private Item itemToCreate;
    private User itemOwner;
    private Booking bookingToCreate;

    private UserResponseDto user;
    Item item;
    BookingResponseDto booking;

    @BeforeEach
    void setUp() {
        itemOwner = new User(1, "Иван", "ivanov.ivan@yandex.ru");
        itemToCreate = new Item(null, "Фотоаппарат", "Nikon", true, itemOwner, null);
        bookingToCreate = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), itemToCreate, itemOwner, BookingStatus.WAITING);
        setBookingElements();
    }

    @Test
    void createBookingTest() {
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking bookingTest = query.setParameter("id", booking.getId())
                .getSingleResult();
        assertThat(booking.getId(), notNullValue());
        assertEquals(bookingTest.getId(), booking.getId());
        assertEquals(bookingTest.getStart(), booking.getStart());
        assertEquals(bookingTest.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingTest.getItem().getId(), booking.getItem().getId());
    }

    @Test
    void approveBookingTest() {
        assertEquals(BookingStatus.WAITING.name(), booking.getStatus().name());
        assertThrows(NotOwnerException.class, () -> bookingService.approveBooking(true, booking.getId(), user.getId() + 100));
        BookingResponseDto responseDto = bookingService.approveBooking(true, booking.getId(), user.getId());
        assertEquals(BookingStatus.APPROVED.name(), responseDto.getStatus().name());
    }

    @Test
    void getBookingByIdTest() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId() + 100, user.getId()));
        assertThrows(NotOwnerException.class, () -> bookingService.getBookingById(booking.getId(), user.getId() + 100));
        assertDoesNotThrow(() -> bookingService.getBookingById(booking.getId(), user.getId()));
    }

    @Test
    void getBookingsForCurrentUserTest() {
        assertEquals(1, bookingService.getBookingsForCurrentUser(user.getId(), "ALL").size());
        bookingService.approveBooking(true, booking.getId(), user.getId());
        assertEquals(1, bookingService.getBookingsForCurrentUser(user.getId(), "CURRENT").size());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsForCurrentUser(user.getId(), "PAST"));
    }

    @Test
    void getBookingsByItemOwnerIdTest() {
        assertEquals(1, bookingService.getBookingsByItemOwnerId(user.getId(), "ALL").size());
        User otherUser = new User(null, "Сергей", "sergey@mail.ru");
        UserResponseDto addedOtherUser = userService.addNewUser(otherUser);
        Item otherItemToCreate = new Item(null, "Фотоаппарат", "Nikon", true, itemOwner, null);
        Booking otherBookingToCreate = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), itemToCreate,
                UserMapper.toDomainModel(addedOtherUser), BookingStatus.WAITING);
        Item otherItem = itemService.addItem(user.getId(), otherItemToCreate, null);
        BookingResponseDto otherBooking = bookingService.createBooking(otherBookingToCreate, addedOtherUser.getId(), otherItem.getId());
        assertEquals(2, bookingService.getBookingsByItemOwnerId(user.getId(), "ALL").size());
    }

    private void setBookingElements() {
        user = userService.addNewUser(itemOwner);
        item = itemService.addItem(user.getId(), itemToCreate, null);
        booking = bookingService.createBooking(bookingToCreate, user.getId(), item.getId());
    }
}
