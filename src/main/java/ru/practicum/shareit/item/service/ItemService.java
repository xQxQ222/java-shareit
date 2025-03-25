package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToShow;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDtoToShow> getUserItems(Integer userId);

    List<Item> getItemsByCaption(String caption);

    ItemDtoToShow getItemById(Integer itemId);

    Item addItem(Integer userId, ItemDto itemDto);

    Item updateItem(Integer userId, ItemDto item, int itemId);

    Comment addComment(Integer authorId, CommentDto commentDto, Integer itemId);
}