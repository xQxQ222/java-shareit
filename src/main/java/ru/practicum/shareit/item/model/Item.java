package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;


@Data
@AllArgsConstructor
public class Item {
    @NonNull
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private boolean available;
    @NonNull
    private User owner;
    @JsonProperty
    private ItemRequest request;
}
