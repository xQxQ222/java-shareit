package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    @Qualifier("DBUser")
    private final UserService userService;


    @GetMapping
    public List<UserRequestDto> getAllUsers() {
        log.info("Пришел GET запрос /users");
        List<UserRequestDto> users = userService.getAllUsers();
        log.info("Отправлен ответ GET /users {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        log.info("Пришел GET запрос /users/{}", userId);
        User user = userService.getUserById(userId);
        log.info("Отправлен ответ GET /users/{} : {}", userId, user);
        return user;
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody UserRequestDto user, @PathVariable Integer userId) {
        log.info("Пришел PATCH запрос /users/{} с телом: {}", userId, user);
        User updatedUser = userService.updateUser(UserMapper.toDomainModel(user, userId));
        log.info("Отправлен ответ PATCH /users/{}: {}", userId, updatedUser);
        return updatedUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User addNewUser(@Valid @RequestBody UserRequestDto newUser) {
        log.info("Пришел POST запрос /users с телом: {}", newUser);
        User addedUser = userService.addNewUser(UserMapper.toDomainModel(newUser, null));
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
