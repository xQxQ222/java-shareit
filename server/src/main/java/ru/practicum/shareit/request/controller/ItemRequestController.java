package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto addNewItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody RequestDto requestDto) {
        log.info("Пришел POST запрос на /requests от пользователя с id {} и телом: {}", userId, requestDto);
        ItemRequestResponseDto newResponse = itemRequestService.addRequest(userId,
                RequestMapper.toRegularModel(null, requestDto, LocalDateTime.now()));
        log.info("Отправлен ответ на POST запрос /requests с телом: {}", newResponse);
        return newResponse;
    }

    @GetMapping
    public List<ItemRequestResponseDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос на /requests от пользователя с id {}", userId);
        List<ItemRequestResponseDto> requests = itemRequestService.getAllUserRequests(userId);
        log.info("Отправлен ответ на GET запрос /requests с телом: {}", requests);
        return requests;
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getOtherRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос на /requests/all от пользователя с id {}", userId);
        List<ItemRequestResponseDto> requests = itemRequestService.getRequestsFromOtherUsers(userId);
        log.info("Отправлен ответ на GET запрос /requests/all с телом: {}", requests);
        return requests;
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Long requestId) {
        log.info("Пришел GET запрос на /requests/{} от пользователя с id {}", requestId, userId);
        ItemRequestResponseDto responseDto = itemRequestService.getRequestById(requestId);
        log.info("Отправлен ответ на GET запрос /requests/{} с телом: {}", requestId, responseDto);
        return responseDto;
    }
}
