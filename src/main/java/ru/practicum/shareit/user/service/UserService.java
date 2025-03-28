package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<UserRequestDto> getAllUsers();

    User getUserById(int id);

    User addNewUser(User newUser);

    void deleteUser(int id);

    User updateUser(User user);
}
