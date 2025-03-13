package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    List<Item> getUsersItems(Integer userId);

    Item getItemById(Integer itemId);

    List<Item> getItemsByText(String caption);

    Item addNewItem(User owner, ItemDto item, ItemRequest request);

    Item updateItem(Integer userId, ItemDto item, int itemId);
}
