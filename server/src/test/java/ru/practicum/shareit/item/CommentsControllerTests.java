package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class CommentsControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment(1, "Отличный перфоратор!", null, null, LocalDateTime.now());
    }

    @Test
    void createCommentTest() throws Exception {

        CommentResponseDto responseDto = new CommentResponseDto(1, comment.getText(), "Никита", "Перфоратор", LocalDateTime.now());

        when(itemService.addComment(anyInt(), any(Comment.class), anyInt()))
                .thenReturn(responseDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId())))
                .andExpect(jsonPath("$.text", is(responseDto.getText())))
                .andExpect(jsonPath("$.authorName", is(responseDto.getAuthorName())))
                .andExpect(jsonPath("$.itemName", is(responseDto.getItemName())));

        verify(itemService, times(1))
                .addComment(anyInt(), any(Comment.class), anyInt());
    }
}
