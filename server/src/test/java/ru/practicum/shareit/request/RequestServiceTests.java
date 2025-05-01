package ru.practicum.shareit.request;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTests {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRequestService itemRequestService;

    private Item itemToCreate;
    private User itemOwner;
    private ItemRequest itemRequest;

    private UserResponseDto user;

    @BeforeEach
    void setUp() {
        itemOwner = new User(1, "Иван", "ivanov.ivan@yandex.ru");
        itemToCreate = new Item(null, "Фотоаппарат", "Nikon", true, itemOwner, null);
        setRequestParams();
    }

    @Test
    void addNewRequest() {
        ItemRequestResponseDto requestResponseDto = itemRequestService.addRequest(user.getId(), itemRequest);
        assertThat(requestResponseDto.getId(), notNullValue());
        TypedQuery<ItemRequest> query = em.createQuery("Select r from ItemRequest r where r.id = :id", ItemRequest.class);
        ItemRequest itemRequestTest = query.setParameter("id", requestResponseDto.getId())
                .getSingleResult();
        assertEquals(requestResponseDto.getId(), itemRequestTest.getId());
        assertEquals(requestResponseDto.getDescription(), itemRequestTest.getDescription());
        assertEquals(requestResponseDto.getCreated(), itemRequestTest.getCreated());
    }

    @Test
    void addItemsByRequest() {
        ItemRequestResponseDto requestResponseDto = itemRequestService.addRequest(user.getId(), itemRequest);
        itemToCreate = new Item(null, "Фотоаппарат", "Nikon", true, null, null);
        Item addedItem = itemService.addItem(user.getId(), itemToCreate, (long) requestResponseDto.getId());
        ItemRequestResponseDto responseDto = itemRequestService.getRequestById((long) requestResponseDto.getId());
        assertEquals(1, responseDto.getItems().size());
    }

    @Test
    void getRequestsFromCurrentUserAndOthersTest() {
        ItemRequestResponseDto requestResponseDto = itemRequestService.addRequest(user.getId(), itemRequest);
        assertEquals(1, itemRequestService.getAllUserRequests(user.getId()).size());
        User otherUserDto = new User(null, "other", "other@yandex.ru");
        UserResponseDto otherUser = userService.addNewUser(otherUserDto);
        assertEquals(0, itemRequestService.getAllUserRequests(otherUser.getId()).size());

        assertEquals(0, itemRequestService.getRequestsFromOtherUsers(user.getId()).size());
        assertEquals(1, itemRequestService.getRequestsFromOtherUsers(otherUser.getId()).size());


    }

    private void setRequestParams() {
        user = userService.addNewUser(itemOwner);
        itemRequest = new ItemRequest(null, "Надо фотоаппарат", null, LocalDateTime.now());
    }
}
