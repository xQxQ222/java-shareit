package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.EmailDuplicateException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service("DBUser")
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(int id) {
        return UserMapper.toResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных")));
    }

    @Override
    @Transactional
    public UserResponseDto addNewUser(User newUser) {
        if (!userRepository.findByEmail(newUser.getEmail()).isEmpty()) {
            throw new EmailDuplicateException("Email " + newUser.getEmail() + " уже занят");
        }
        return UserMapper.toResponseDto(userRepository.save(newUser));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(User user) {
        User userFromDb = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Такого пользователя нет в БД"));
        if (user.getName() != null && !user.getName().isBlank()) {
            userFromDb.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userFromDb.setEmail(user.getEmail());
        }
        return UserMapper.toResponseDto(userRepository.save(userFromDb));
    }
}
