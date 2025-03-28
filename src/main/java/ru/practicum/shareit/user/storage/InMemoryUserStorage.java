package ru.practicum.shareit.user.storage;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<Integer, String> userEmails = new HashMap<>();

    private int idCounter = 0;

    @Override
    public List<UserRequestDto> getAll() {
        return users.values().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public User getById(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public User add(UserRequestDto newUser) {
        if (newUser.getEmail() == null) {
            throw new ValidationException("Не указана почта пользователя");
        }
        if (newUser.getName() == null) {
            throw new ValidationException("Не указано имя пользователя");
        }
        if (userEmails.values().contains(newUser.getEmail())) {
            throw new AlreadyExistsException("Пользователь с Email " + newUser.getEmail() + " уже существует");
        }
        int newId = getNextId();
        User user = UserMapper.toDomainModel(newUser, newId);
        users.put(newId, user);
        userEmails.put(newId, newUser.getEmail());
        return user;
    }

    @Override
    public void delete(Integer id) {
        users.remove(id);
        userEmails.remove(id);
    }

    @Override
    public User update(UserRequestDto userRequestDto, int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не существует");
        }
        if (userEmails.containsValue(userRequestDto.getEmail()) && !userEmails.get(userId).equals(userRequestDto.getEmail()) && userRequestDto.getEmail() != null) {
            throw new AlreadyExistsException("Другой пользователь уже использует почту " + userRequestDto.getEmail());
        }
        User userFromCollection = users.get(userId);
        User user = UserMapper.toDomainModel(userRequestDto, userId);
        if (user.getName() == null) {
            user.setName(userFromCollection.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userFromCollection.getEmail());
        }
        users.put(userId, user);
        userEmails.put(userId, user.getEmail());
        return user;
    }

    @Override
    public User getFullUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return users.get(userId);
    }

    private int getNextId() {
        return ++idCounter;
    }


}
