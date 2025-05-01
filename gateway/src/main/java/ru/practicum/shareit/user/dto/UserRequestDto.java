package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRequestDto {
    @NotBlank
    @NonNull
    private final String name;
    @NonNull
    @Email
    private final String email;
}
