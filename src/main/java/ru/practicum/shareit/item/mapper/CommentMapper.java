package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toRegularModel(Integer id, User author, CommentDto dto, Item item, LocalDateTime createDate) {
        return new Comment(
                id,
                dto.getText(),
                item,
                author,
                createDate
        );
    }
}
