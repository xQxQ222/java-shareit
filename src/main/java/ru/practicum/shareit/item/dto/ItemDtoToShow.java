package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDtoToShow {
    private final int id;
    private final String name;
    private final String description;
    private final boolean available;
    private final User owner;
    private final ItemRequest request;
    private final LocalDateTime lastBooking;
    private final LocalDateTime nextBooking;
    private final List<Comment> comments;
}
