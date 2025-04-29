package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@WebMvcTest(ItemController.class)
public class ItemControllerTests {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private Item itemToCreate;
    private User itemOwner;
    private ItemResponseDto itemResponseDto;

    @BeforeEach
    void setUp() {
        itemOwner = new User(1, "Ivan", "ivanov.ivan@gmail.com");
        itemToCreate = new Item(1, "Камера", "Камера go pro", true, itemOwner, null);
        itemResponseDto = ItemMapper.toResponseDto(itemToCreate, new Booking(), new Booking(), List.of());
    }

    @Test
    void addNewItemTest() throws Exception {

        ItemRequestDto requestDto = new ItemRequestDto("Камера", "Камера go pro", true, 1);

        when(itemService.addItem(anyInt(), any(Item.class), anyLong()))
                .thenReturn(itemToCreate);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemToCreate.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemToCreate.getName())))
                .andExpect(jsonPath("$.description", is(itemToCreate.getDescription())))
                .andExpect(jsonPath("$.available", is(itemToCreate.getAvailable())));

        verify(itemService, times(1))
                .addItem(anyInt(), any(Item.class), anyLong());

    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemResponseDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId())))
                .andExpect(jsonPath("$.name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())));

        verify(itemService, times(1))
                .getItemById(anyInt(), anyInt());
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(anyInt(), any(Item.class)))
                .thenReturn(itemToCreate);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemToCreate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemToCreate.getId())));

        verify(itemService, times(1))
                .updateItem(anyInt(), any(Item.class));
    }

    @Test
    void getAllUserItemsTest() throws Exception {
        when(itemService.getUserItems(anyInt()))
                .thenReturn(List.of(itemResponseDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemService, times(1))
                .getUserItems(anyInt());
    }

    @Test
    void getItemsByCaptionTest() throws Exception {
        when(itemService.getItemsByCaption(anyString()))
                .thenReturn(List.of(itemToCreate));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(get("/items/search?text=printer")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemService, times(2))
                .getItemsByCaption(anyString());
    }
}
