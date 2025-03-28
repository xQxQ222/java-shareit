package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<UserRequestDto> getAll();

    User getById(Integer id);

    User add(UserRequestDto newUser);

    void delete(Integer id);

    User update(UserRequestDto user, int userId);

    User getFullUserById(Integer id);
}
