package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Пришел GET запрос /users");
        List<UserDto> users = userService.getAllUsers();
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
    public User updateUser(@Valid @RequestBody UserDto user, @PathVariable Integer userId) {
        log.info("Пришел PATCH запрос /users/{} с телом: {}", userId, user);
        User updatedUser = userService.updateUser(user, userId);
        log.info("Отправлен ответ PATCH /users/{}: {}", userId, updatedUser);
        return updatedUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User addNewUser(@Valid @RequestBody UserDto newUser) {
        log.info("Пришел POST запрос /users с телом: {}", newUser);
        User addedUser = userService.addNewUser(newUser);
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
