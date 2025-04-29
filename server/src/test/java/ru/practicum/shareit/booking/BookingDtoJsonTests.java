package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoJsonTests {

    private final JacksonTester<BookingResponseDto> bookingResponseDtoJacksonTester;

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
}
