package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NonNull
    private Boolean available;
    private Integer requestId;
}
