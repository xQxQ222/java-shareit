package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;
    @NotBlank(message = "Комментарий не должен быть пустым")
    private String text;
}
