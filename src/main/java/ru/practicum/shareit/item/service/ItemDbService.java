package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service("DBItem")
@RequiredArgsConstructor
public class ItemDbService implements ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public List<Item> getUserItems(Integer userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public List<Item> getItemsByCaption(String caption) {
        if (caption.isBlank()) {
            return List.of();
        }
        return itemRepository.searchByPattern(caption);
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
    }

    @Override
    public Item addItem(Integer userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        return itemRepository.save(ItemMapper.toRegularModel(null, itemDto, owner, null));
    }

    @Override
    public Item updateItem(Integer userId, ItemDto item, int itemId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        return itemRepository.save(ItemMapper.toRegularModel(itemId, item, owner, null));
    }
}
