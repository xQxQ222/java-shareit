package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShowDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToShow;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoToShow> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос /items от пользователя с id: {}", userId);
        List<ItemDtoToShow> items = itemService.getUserItems(userId);
        log.info("Отправлен ответ на GET запрос /items: {}", items);
        return items;
    }

    @GetMapping("/{itemId}")
    public ItemDtoToShow getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId) {
        log.info("Пришел GET запрос /items/{} от пользователя с id: {}", itemId, userId);
        ItemDtoToShow item = itemService.getItemById(itemId, userId);
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
    public Item addNewItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Пришел POST запрос /items от пользователя с id: {} с телом: {}", userId, itemDto);
        Item item = itemService.addItem(userId, itemDto);
        log.info("Отправлен ответ на POST запрос /items: {}", item);
        return item;
    }

    @PostMapping("/{itemId}/comment")
    public CommentShowDto addNewComment(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId, @RequestBody CommentDto dto) {
        log.info("Пришел POST запрос /items/{}/comment от пользователя с id: {} с телом: {}", itemId, userId, dto);
        CommentShowDto comment = itemService.addComment(userId, dto, itemId);
        log.info("Отправлен ответ на POST запрос /items/{}/comment: {}", itemId, comment);
        return comment;
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemDto itemToUpdate, @PathVariable int itemId) {
        log.info("Пришел PATCH запрос /items от пользователя с id: {} с телом: {}", userId, itemToUpdate);
        Item item = itemService.updateItem(userId, itemToUpdate, itemId);
        log.info("Отправлен ответ на PATCH запрос /items: {}", item);
        return item;
    }


}
