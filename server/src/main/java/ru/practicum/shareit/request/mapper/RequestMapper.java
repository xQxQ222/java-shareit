package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {
    public static ItemRequest toRegularModel(Integer id, RequestDto dto, LocalDateTime created) {
        return new ItemRequest(
                id,
                dto.getDescription(),
                null,
                created
        );
    }

    public static ItemRequestResponseDto toResponseDto(ItemRequest itemRequest, List<Item> items) {
        return new ItemRequestResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }
}
