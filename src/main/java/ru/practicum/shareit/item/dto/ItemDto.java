package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private Boolean available;
    @JsonProperty
    private Integer requestId;
}
