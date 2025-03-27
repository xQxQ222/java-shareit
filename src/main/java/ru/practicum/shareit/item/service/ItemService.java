package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemResponseDto> getUserItems(Integer userId);

    List<Item> getItemsByCaption(String caption);

    ItemResponseDto getItemById(Integer itemId, Integer userId);

    Item addItem(Integer userId, Item item);

    Item updateItem(Integer userId, Item item);

    CommentResponseDto addComment(Integer authorId, Comment commentRequestDto, Integer itemId);
}