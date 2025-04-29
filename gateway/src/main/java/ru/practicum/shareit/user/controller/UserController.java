package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;


    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Пришел gateway GET запрос /users");
        ResponseEntity<Object> users = userClient.getAllUsers();
        log.info("Отправлен ответ GET /users {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        log.info("Пришел gateway GET запрос /users/{}", userId);
        ResponseEntity<Object> user = userClient.getUserById(userId);
        log.info("Отправлен ответ GET /users/{} : {}", userId, user);
        return user;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserUpdateRequestDto user, @PathVariable Integer userId) {
        log.info("Пришел gateway PATCH запрос /users/{} с телом: {}", userId, user);
        ResponseEntity<Object> updatedUser = userClient.updateUser(user, userId);
        log.info("Отправлен ответ PATCH /users/{}: {}", userId, updatedUser);
        return updatedUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody UserRequestDto newUser) {
        log.info("Пришел gateway POST запрос /users с телом: {}", newUser);
        ResponseEntity<Object> addedUser = userClient.addNewUser(newUser);
        log.info("Отправлен ответ POST /users: {}", addedUser);
        return addedUser;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Пришел gateway DELETE запрос /users/{}", userId);
        userClient.deleteUser(userId);
        log.info("Пользователь с id {} успешно удален", userId);
    }

}
