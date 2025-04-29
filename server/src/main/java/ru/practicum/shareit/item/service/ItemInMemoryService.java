package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
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


    public List<ItemResponseDto> getUserItems(Integer userId) {
        return null;
    }

    public List<Item> getItemsByCaption(String caption) {
        return itemStorage.getItemsByText(caption);
    }

    public ItemResponseDto getItemById(Integer itemId, Integer userId) {
        return null;
    }

    @Override
    public Item addItem(Integer userId, Item item, Long requestId) {
        return null;
    }

    @Override
    public Item updateItem(Integer userId, Item item) {
        return null;
    }

    public Item addItem(Integer userId, ItemRequestDto itemRequestDto) {
        User owner = userStorage.getFullUserById(userId);
        return itemStorage.addNewItem(owner, itemRequestDto, null);
    }

    public Item updateItem(Integer userId, ItemRequestDto item, int itemId) {
        return itemStorage.updateItem(userId, item, itemId);
    }

    @Override
    public CommentResponseDto addComment(Integer authorId, Comment dto, Integer itemId) {
        return null;
    }
}
