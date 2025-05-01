package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
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
import ru.practicum.shareit.exception.exceptions.FalseBookerException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceTests {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private Item itemToCreate;
    private User itemOwner;
    private User bookerRequest;
    private Booking bookingToCreate;

    private UserResponseDto user;
    private UserResponseDto booker;
    Item item;
    BookingResponseDto booking;

    @BeforeEach
    void setUp() {

        setBookingElements();
    }

    @Test
    void createCommentsTest() {
        Comment commentRequestDto = new Comment(null, "Хорошая вещь!", null, null, LocalDateTime.now());

        assertThrows(FalseBookerException.class, () -> itemService.addComment(booker.getId(), commentRequestDto, item.getId()));
        bookingService.approveBooking(true, booking.getId(), user.getId());
        CommentResponseDto comment = itemService.addComment(booker.getId(), commentRequestDto, item.getId());
        assertThat(comment.getId(), notNullValue());
        ItemResponseDto dto = itemService.getItemById(item.getId(), user.getId());
        assertEquals(1, dto.getComments().size());
    }

    private void setBookingElements() {
        itemOwner = new User(null, "Иван", "ivanov.ivan@yandex.ru");
        bookerRequest = new User(null, "Роман", "mrmart@mail.ru");
        user = userService.addNewUser(itemOwner);
        booker = userService.addNewUser(bookerRequest);

        itemToCreate = new Item(null, "Фотоаппарат", "Nikon", true, itemOwner, null);
        item = itemService.addItem(user.getId(), itemToCreate, null);

        bookingToCreate = new Booking(null, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1).plusSeconds(1), itemToCreate,
                UserMapper.toDomainModel(booker), BookingStatus.WAITING);
        booking = bookingService.createBooking(bookingToCreate, booker.getId(), item.getId());
    }
}
