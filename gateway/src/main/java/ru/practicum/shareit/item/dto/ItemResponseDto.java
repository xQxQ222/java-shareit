package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemResponseDto {
    private final int id;
    private final String name;
    private final String description;
    private final boolean available;
    private final UserResponseDto owner;
    private final ItemRequestResponseDto request;
    private final LocalDateTime lastBooking;
    private final LocalDateTime nextBooking;
    private final List<CommentResponseDto> comments;
}
