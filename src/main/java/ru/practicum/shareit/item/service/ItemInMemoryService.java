package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service("inMemoryItem")
@RequiredArgsConstructor
public class ItemInMemoryService implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;


    public List<Item> getUserItems(Integer userId) {
        return itemStorage.getUsersItems(userId);
    }

    public List<Item> getItemsByCaption(String caption) {
        return itemStorage.getItemsByText(caption);
    }

    public Item getItemById(Integer itemId) {
        return itemStorage.getItemById(itemId);
    }

    public Item addItem(Integer userId, ItemDto itemDto) {
        User owner = userStorage.getFullUserById(userId);
        return itemStorage.addNewItem(owner, itemDto, null);
    }

    public Item updateItem(Integer userId, ItemDto item, int itemId) {
        return itemStorage.updateItem(userId, item, itemId);
    }
}
