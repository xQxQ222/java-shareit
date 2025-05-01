package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final RequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addNewItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody RequestDto requestDto) {
        log.info("Пришел POST запрос на /requests от пользователя с id {} и телом: {}", userId, requestDto);
        ResponseEntity<Object> newResponse = itemRequestClient.addRequest(userId, requestDto);
        log.info("Отправлен ответ на POST запрос /requests с телом: {}", newResponse);
        return newResponse;
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос на /requests от пользователя с id {}", userId);
        ResponseEntity<Object> requests = itemRequestClient.getAllUserRequests(userId);
        log.info("Отправлен ответ на GET запрос /requests с телом: {}", requests);
        return requests;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос на /requests/all от пользователя с id {}", userId);
        ResponseEntity<Object> requests = itemRequestClient.getRequestsFromOtherUsers(userId);
        log.info("Отправлен ответ на GET запрос /requests/all с телом: {}", requests);
        return requests;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Long requestId) {
        log.info("Пришел GET запрос на /requests/{} от пользователя с id {}", requestId, userId);
        ResponseEntity<Object> responseDto = itemRequestClient.getRequestById(userId, requestId);
        log.info("Отправлен ответ на GET запрос /requests/{} с телом: {}", requestId, responseDto);
        return responseDto;
    }
}
