package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private final String name;
    private final String email;
}
