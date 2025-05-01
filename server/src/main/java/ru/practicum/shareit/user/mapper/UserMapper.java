package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserRequestDto toDto(User user) {
        return new UserRequestDto(
                user.getName(),
                user.getEmail()
        );
    }

    public static User toDomainModel(UserRequestDto userRequestDto, Integer userId) {
        return new User(
                userId,
                userRequestDto.getName(),
                userRequestDto.getEmail()
        );
    }

    public static User toDomainModel(UserResponseDto responseDto) {
        return new User(
                responseDto.getId(),
                responseDto.getName(),
                responseDto.getEmail()
        );
    }

    public static UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
