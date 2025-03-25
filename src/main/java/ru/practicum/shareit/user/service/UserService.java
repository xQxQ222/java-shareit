package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    User getUserById(int id);

    User addNewUser(UserDto newUser);

    void deleteUser(int id);

    User updateUser(UserDto user, int userId);
}
