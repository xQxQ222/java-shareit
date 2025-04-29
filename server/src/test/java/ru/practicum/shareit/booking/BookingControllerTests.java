package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingResponseDto bookingResponseDto;
    private User booker;
    private Item bookingItem;

    @BeforeEach
    void setUp() {

        bookingResponseDto = new BookingResponseDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), BookingStatus.APPROVED,
                new UserResponseDto(1, "Толик", "tolyan@mail.com"),
                new ItemResponseShortDto(1, "Дрель", "Новая дрель", true));

        booker = new User(2, "Сергей", "sergey@mail.ru");

        bookingItem = new Item(2, "test", "test", true, booker, new ItemRequest(1, "fsad", booker, LocalDateTime.now()));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(bookingResponseDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId())))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().name())))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", is(bookingResponseDto.getBooker().getName())))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(bookingResponseDto.getItem().getName())));

        verify(bookingService, times(1))
                .getBookingById(anyInt(), anyInt());
    }


    @Test
    void getBookingsByCurrentUserTest() throws Exception {
        when(bookingService.getBookingsForCurrentUser(anyInt(), anyString()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(get("/bookings?state=PAST")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookingService, times(2))
                .getBookingsForCurrentUser(anyInt(), anyString());
    }

    @Test
    void getBookingByItemOwnerTest() throws Exception {
        when(bookingService.getBookingsByItemOwnerId(anyInt(), anyString()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(get("/bookings/owner?state=PAST")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookingService, times(2))
                .getBookingsByItemOwnerId(anyInt(), anyString());
    }

    @Test
    void changeBookingStatusTest() throws Exception {
        when(bookingService.approveBooking(anyBoolean(), anyInt(), anyInt()))
                .thenReturn(bookingResponseDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId())))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().name())));

        verify(bookingService, times(1))
                .approveBooking(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    void createNewBookingTest() throws Exception {
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), bookingItem, booker, BookingStatus.APPROVED);
        BookingRequestDto requestDto = new BookingRequestDto(3, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingService.createBooking(any(Booking.class), anyInt(), anyInt()))
                .thenReturn(bookingResponseDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId())))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().name())))
                .andExpect(jsonPath("$.booker.name", is(bookingResponseDto.getBooker().getName())))
                .andExpect(jsonPath("$.item.name", is(bookingResponseDto.getItem().getName())));

        verify(bookingService, times(1))
                .createBooking(any(Booking.class), anyInt(), anyInt());

    }

    @Test
    void bookingWithoutUserHeader() throws Exception {
        when(bookingService.createBooking(any(Booking.class), anyInt(), anyInt()))
                .thenReturn(bookingResponseDto);

        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), bookingItem, booker, BookingStatus.APPROVED);
        BookingRequestDto requestDto = new BookingRequestDto(3, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingService.createBooking(any(Booking.class), anyInt(), anyInt()))
                .thenReturn(bookingResponseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
