package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    @Qualifier(value = "DBUser")
    private final UserService userService;


    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        log.info("Пришел GET запрос /users");
        List<UserResponseDto> users = userService.getAllUsers();
        log.info("Отправлен ответ GET /users {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable Integer userId) {
        log.info("Пришел GET запрос /users/{}", userId);
        UserResponseDto user = userService.getUserById(userId);
        log.info("Отправлен ответ GET /users/{} : {}", userId, user);
        return user;
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@RequestBody UserRequestDto user, @PathVariable Integer userId) {
        log.info("Пришел PATCH запрос /users/{} с телом: {}", userId, user);
        UserResponseDto updatedUser = userService.updateUser(UserMapper.toDomainModel(user, userId));
        log.info("Отправлен ответ PATCH /users/{}: {}", userId, updatedUser);
        return updatedUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto addNewUser(@RequestBody UserRequestDto newUser) {
        log.info("Пришел POST запрос /users с телом: {}", newUser);
        UserResponseDto addedUser = userService.addNewUser(UserMapper.toDomainModel(newUser, null));
        log.info("Отправлен ответ POST /users: {}", addedUser);
        return addedUser;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с id {} успешно удален", userId);
    }

}
