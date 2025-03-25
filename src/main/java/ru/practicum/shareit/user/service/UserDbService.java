package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service("DBUser")
@Primary
@RequiredArgsConstructor
public class UserDbService implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
    }

    @Override
    public User addNewUser(UserDto newUser) {
        return userRepository.save(UserMapper.toRegularModel(newUser, 0));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(UserDto user, int userId) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в БД"));
        if (user.getName() != null && !user.getName().isBlank()) {
            userFromDb.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userFromDb.setEmail(user.getEmail());
        }
        return userRepository.save(userFromDb);
    }
}
