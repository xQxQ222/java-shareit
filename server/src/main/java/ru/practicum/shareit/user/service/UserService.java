package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(int id);

    UserResponseDto addNewUser(User newUser);

    void deleteUser(int id);

    UserResponseDto updateUser(User user);
}
