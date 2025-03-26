package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShowDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToShow;
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


    public List<ItemDtoToShow> getUserItems(Integer userId) {
        return null;
    }

    public List<Item> getItemsByCaption(String caption) {
        return itemStorage.getItemsByText(caption);
    }

    public ItemDtoToShow getItemById(Integer itemId, Integer userId) {
        return null;
    }

    public Item addItem(Integer userId, ItemDto itemDto) {
        User owner = userStorage.getFullUserById(userId);
        return itemStorage.addNewItem(owner, itemDto, null);
    }

    public Item updateItem(Integer userId, ItemDto item, int itemId) {
        return itemStorage.updateItem(userId, item, itemId);
    }

    @Override
    public CommentShowDto addComment(Integer authorId, CommentDto dto, Integer itemId) {
        return null;
    }
}
