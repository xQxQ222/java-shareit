package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toDomainModel(Integer id, User author, CommentRequestDto dto, Item item, LocalDateTime createDate) {
        return new Comment(
                id,
                dto.getText(),
                item,
                author,
                createDate
        );
    }

    public static CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getItem().getName(),
                comment.getCreated()
        );

    }
}
