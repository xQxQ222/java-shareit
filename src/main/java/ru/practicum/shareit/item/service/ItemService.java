package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getUserItems(Integer userId);
    List<Item> getItemsByCaption(String caption);
    Item getItemById(Integer itemId);
    Item addItem(Integer userId, ItemDto itemDto);
    Item updateItem(Integer userId, ItemDto item, int itemId);
}
