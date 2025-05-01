package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoJsonTests {

    private final JacksonTester<BookingResponseDto> bookingResponseDtoJacksonTester;
    private final JacksonTester<BookingRequestDto> bookingRequestDtoJacksonTester;

    @Test
    void bookingResponseDtoJsonTest() throws Exception {
        UserResponseDto userRD = new UserResponseDto(1, "Ivan", "ivanov@mail.com");
        ItemResponseShortDto itemRSD = new ItemResponseShortDto(1, "test", "test", true);
        BookingResponseDto bookingResponseDto = new BookingResponseDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                BookingStatus.WAITING, userRD, itemRSD);
        JsonContent<BookingResponseDto> result = bookingResponseDtoJacksonTester.write(bookingResponseDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
    }

    @Test
    void bookingRequestDtoJsonTest() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        JsonContent<BookingRequestDto> result = bookingRequestDtoJacksonTester.write(bookingRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

    @Test
    void mapperRequestDtoTest() {
        Item item = new Item(1, "sfa", "afd", true, null, null);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                item, null, BookingStatus.WAITING);
        BookingRequestDto dto = BookingMapper.toDtoModel(booking);
        assertEquals(booking.getItem().getId(), dto.getItemId());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
    }
}
