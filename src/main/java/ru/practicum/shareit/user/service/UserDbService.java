package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service("DBUser")
@RequiredArgsConstructor
public class UserDbService implements UserService {

    private UserRepository userRepository;


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
    }

    @Override
    public User addNewUser(UserDto newUser) {
        return userRepository.save(UserMapper.toRegularModel(newUser, null));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(UserDto user, int userId) {
        return userRepository.save(UserMapper.toRegularModel(user, userId));
    }
}
