package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class RequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestResponseDto responseDto;

    @BeforeEach
    void setUp() {
        Item item = new Item(1, "test", "test", true, null, null);
        responseDto = new ItemRequestResponseDto(1, "Надо фотоаппарат", LocalDateTime.now(), List.of(item));
    }

    @Test
    void addNewItemRequestTest() throws Exception {
        when(itemRequestService.addRequest(anyInt(), any(ItemRequest.class)))
                .thenReturn(responseDto);

        RequestDto dto = new RequestDto("Надо фотоаппарат");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId())))
                .andExpect(jsonPath("$.description", is(responseDto.getDescription())))
                .andExpect(jsonPath("$.items.length()").value(1));

        verify(itemRequestService, times(1))
                .addRequest(anyInt(), any(ItemRequest.class));
    }

    @Test
    void getUserRequestsTest() throws Exception {
        when(itemRequestService.getAllUserRequests(anyInt()))
                .thenReturn(List.of(responseDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemRequestService, times(1))
                .getAllUserRequests(anyInt());
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(itemRequestService.getRequestById(anyLong()))
                .thenReturn(responseDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.id", is(responseDto.getId())))
                .andExpect(jsonPath("$.description", is(responseDto.getDescription())))
                .andExpect(jsonPath("$.items.length()").value(1));

        verify(itemRequestService, times(1))
                .getRequestById(anyLong());
    }

    @Test
    void getOtherRequestsTest() throws Exception {
        when(itemRequestService.getRequestsFromOtherUsers(anyInt()))
                .thenReturn(List.of(responseDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(itemRequestService, times(1))
                .getRequestsFromOtherUsers(anyInt());
    }
}
