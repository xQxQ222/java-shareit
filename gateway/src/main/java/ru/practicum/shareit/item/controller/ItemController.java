package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestUpdateDto;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос /items от пользователя с id: {}", userId);
        ResponseEntity<Object> items = itemClient.getUserItems(userId);
        log.info("Отправлен ответ на GET запрос /items: {}", items);
        return items;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId) {
        log.info("Пришел GET запрос /items/{} от пользователя с id: {}", itemId, userId);
        ResponseEntity<Object> item = itemClient.getItemById(itemId, userId);
        log.info("Отправлен ответ на GET запрос /items/{}: {}", itemId, item);
        return item;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByCaption(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam String text) {
        log.info("Пришел GET запрос /items/search?text={} от пользователя с id: {}", text, userId);
        ResponseEntity<Object> items = itemClient.getItemsByCaption(text, userId);
        log.info("Отправлен ответ на GET запрос /items/search?text={}: {}", text, items);
        return items;
    }

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Пришел POST запрос /items от пользователя с id: {} с телом: {}", userId, itemRequestDto);
        ResponseEntity<Object> item = itemClient.addItem(userId, itemRequestDto);
        log.info("Отправлен ответ на POST запрос /items: {}", item);
        return item;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addNewComment(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @PathVariable Integer itemId, @RequestBody CommentRequestDto dto) {
        log.info("Пришел POST запрос /items/{}/comment от пользователя с id: {} с телом: {}", itemId, userId, dto);
        ResponseEntity<Object> comment = itemClient.addComment(itemId, userId, dto);
        log.info("Отправлен ответ на POST запрос /items/{}/comment: {}", itemId, comment);
        return comment;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemRequestUpdateDto itemToUpdate, @PathVariable int itemId) {
        log.info("Пришел PATCH запрос /items от пользователя с id: {} с телом: {}", userId, itemToUpdate);
        ResponseEntity<Object> item = itemClient.updateItem(userId, itemToUpdate, itemId);
        log.info("Отправлен ответ на PATCH запрос /items: {}", item);
        return item;
    }


}
