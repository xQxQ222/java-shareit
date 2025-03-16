package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;


    public List<UserDto> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getById(id);
    }

    public User addNewUser(UserDto newUser) {
        return userStorage.add(newUser);
    }

    public void deleteUser(int id) {
        userStorage.delete(id);
    }

    public User updateUser(UserDto user, int userId) {
        return userStorage.update(user, userId);
    }
}
