package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
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
                .filter(Item::getAvailable)
                .toList();
    }

    @Override
    public Item addNewItem(User owner, ItemRequestDto item, ItemRequest request) {
        int newId = getNextItemId();
        Item addedItem = ItemMapper.toDomainModel(newId, item, owner, request);
        items.put(newId, addedItem);
        return addedItem;
    }

    @Override
    public Item updateItem(Integer userId, ItemRequestDto itemRequestDto, int itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмет с id " + itemId + " не найден в хранилище");
        }
        Item itemFromCollection = getItemById(itemId);
        if (itemFromCollection.getOwner().getId() != userId) {
            throw new NotOwnerException("Пользователь с id " + userId + " не является владельцем " + itemFromCollection.getName());
        }
        if (itemRequestDto.getDescription() == null) {
            itemRequestDto.setDescription(itemFromCollection.getDescription());
        }
        if (itemRequestDto.getName() == null) {
            itemRequestDto.setName(itemFromCollection.getName());
        }
        if (itemRequestDto.getAvailable() == null) {
            itemRequestDto.setAvailable(itemFromCollection.getAvailable());
        }
        Item newItem = ItemMapper.toDomainModel(itemId, itemRequestDto, itemFromCollection.getOwner(), itemFromCollection.getRequest());
        items.put(itemId, newItem);
        return newItem;
    }


    private int getNextItemId() {
        return ++itemsCount;
    }
}
