package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BookingDto {
    private Integer itemId;
    private Instant start;
    private Instant end;
    @JsonIgnore
    private Integer bookerId;
    @JsonIgnore
    private BookingStatus status = BookingStatus.WAITING;
}
