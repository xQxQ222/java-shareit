package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service("inMemoryUser")
@RequiredArgsConstructor
public class UserInMemoryService implements UserService {
    private final UserStorage userStorage;


    public List<UserRequestDto> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getById(id);
    }

    @Override
    public User addNewUser(User newUser) {
        return null;
    }


    public void deleteUser(int id) {
        userStorage.delete(id);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
