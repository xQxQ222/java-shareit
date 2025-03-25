package ru.practicum.shareit.comment.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Entity
@Data
@Table(name="comments")
public class Comment {
    @Id
    private int id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item commentItem;
}
