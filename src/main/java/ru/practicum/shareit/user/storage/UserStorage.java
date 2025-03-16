package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<UserDto> getAll();

    User getById(Integer id);

    User add(UserDto newUser);

    void delete(Integer id);

    User update(UserDto user, int userId);

    User getFullUserById(Integer id);
}
