package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос /items от пользователя с id: {}", userId);
        List<ItemResponseDto> items = itemService.getUserItems(userId);
        log.info("Отправлен ответ на GET запрос /items: {}", items);
        return items;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId) {
        log.info("Пришел GET запрос /items/{} от пользователя с id: {}", itemId, userId);
        ItemResponseDto item = itemService.getItemById(itemId, userId);
        log.info("Отправлен ответ на GET запрос /items/{}: {}", itemId, item);
        return item;
    }

    @GetMapping("/search")
    public List<Item> getItemsByCaption(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam String text) {
        log.info("Пришел GET запрос /items/search?text={} от пользователя с id: {}", text, userId);
        List<Item> items = itemService.getItemsByCaption(text);
        log.info("Отправлен ответ на GET запрос /items/search?text={}: {}", text, items);
        return items;
    }

    @PostMapping
    public Item addNewItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Пришел POST запрос /items от пользователя с id: {} с телом: {}", userId, itemRequestDto);
        Item item = itemService.addItem(userId, ItemMapper.toDomainModel(null, itemRequestDto, null, null));
        log.info("Отправлен ответ на POST запрос /items: {}", item);
        return item;
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addNewComment(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId, @RequestBody CommentRequestDto dto) {
        log.info("Пришел POST запрос /items/{}/comment от пользователя с id: {} с телом: {}", itemId, userId, dto);
        CommentResponseDto comment = itemService.addComment(userId, CommentMapper.toDomainModel(null, null, dto, null, LocalDateTime.now()), itemId);
        log.info("Отправлен ответ на POST запрос /items/{}/comment: {}", itemId, comment);
        return comment;
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemRequestDto itemToUpdate, @PathVariable int itemId) {
        log.info("Пришел PATCH запрос /items от пользователя с id: {} с телом: {}", userId, itemToUpdate);
        Item item = itemService.updateItem(userId, ItemMapper.toDomainModel(itemId, itemToUpdate, null, null));
        log.info("Отправлен ответ на PATCH запрос /items: {}", item);
        return item;
    }


}
