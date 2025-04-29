package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service("inMemoryUser")
@RequiredArgsConstructor
public class UserInMemoryService implements UserService {
    private final UserStorage userStorage;


    public List<UserResponseDto> getAllUsers() {
        return null;
    }

    public UserResponseDto getUserById(int id) {
        return null;
    }

    @Override
    public UserResponseDto addNewUser(User newUser) {
        return null;
    }


    public void deleteUser(int id) {
        userStorage.delete(id);
    }

    @Override
    public UserResponseDto updateUser(User user) {
        return null;
    }
}
