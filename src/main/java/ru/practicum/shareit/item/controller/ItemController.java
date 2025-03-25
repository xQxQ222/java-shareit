package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    @Qualifier("DBItem")
    private final ItemService itemService;


    @GetMapping
    public List<Item> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Пришел GET запрос /items от пользователя с id: {}", userId);
        List<Item> items = itemService.getUserItems(userId);
        log.info("Отправлен ответ на GET запрос /items: {}", items);
        return items;
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer itemId) {
        log.info("Пришел GET запрос /items/{} от пользователя с id: {}", itemId, userId);
        Item item = itemService.getItemById(itemId);
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

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemDto itemToUpdate, @PathVariable int itemId) {
        log.info("Пришел PATCH запрос /items от пользователя с id: {} с телом: {}", userId, itemToUpdate);
        Item item = itemService.updateItem(userId, itemToUpdate, itemId);
        log.info("Отправлен ответ на PATCH запрос /items: {}", item);
        return item;
    }


}
