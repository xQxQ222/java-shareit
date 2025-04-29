package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static ItemRequestDto toRequestDto(Item item) {
        return new ItemRequestDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toDomainModel(Integer itemId, ItemRequestDto itemRequestDto, User owner, ItemRequest request) {
        return new Item(
                itemId,
                itemRequestDto.getName(),
                itemRequestDto.getDescription(),
                itemRequestDto.getAvailable(),
                owner,
                request
        );
    }

    public static ItemResponseDto toResponseDto(Item item, Booking last, Booking next, List<Comment> comments) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest(),
                last == null ? null : last.getEnd(),
                next == null ? null : next.getEnd(),
                comments
        );
    }

    public static ItemResponseShortDto toShortDto(Item item) {
        return new ItemResponseShortDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

}
