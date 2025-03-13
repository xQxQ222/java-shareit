package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final HashMap<Integer, Item> items = new HashMap<>();
    private int itemsCount = 0;

    @Override
    public List<Item> getUsersItems(Integer userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
    }

    @Override
    public Item getItemById(Integer itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмет с id " + itemId + " не найден в хранилище");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsByText(String caption) {
        if (caption.isBlank()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> item.getName().toUpperCase().contains(caption.toUpperCase()) || item.getDescription().toUpperCase().contains(caption.toUpperCase()))
                .filter(Item::isAvailable)
                .toList();
    }

    @Override
    public Item addNewItem(User owner, ItemDto item, ItemRequest request) {
        int newId = getNextItemId();
        Item addedItem = ItemMapper.toRegularModel(newId, item, owner, request);
        items.put(newId, addedItem);
        return addedItem;
    }

    @Override
    public Item updateItem(Integer userId, ItemDto itemDto, int itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмет с id " + itemId + " не найден в хранилище");
        }
        Item itemFromCollection = getItemById(itemId);
        if (itemFromCollection.getOwner().getId() != userId) {
            throw new NotOwnerException("Пользователь с id " + userId + " не является владельцем " + itemFromCollection.getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemFromCollection.getDescription());
        }
        if (itemDto.getName() == null) {
            itemDto.setName(itemFromCollection.getName());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemFromCollection.isAvailable());
        }
        Item newItem = ItemMapper.toRegularModel(itemId, itemDto, itemFromCollection.getOwner(), itemFromCollection.getRequest());
        items.put(itemId, newItem);
        return newItem;
    }


    private int getNextItemId() {
        return ++itemsCount;
    }
}
