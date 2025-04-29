package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemResponseShortDto {
    private final Integer id;
    private final String name;
    private final String description;
    private final Boolean isAvailable;
}
