package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    List<Item> getUsersItems(Integer userId);

    Item getItemById(Integer itemId);

    List<Item> getItemsByText(String caption);

    Item addNewItem(User owner, ItemRequestDto item, ItemRequest request);

    Item updateItem(Integer userId, ItemRequestDto item, int itemId);
}
