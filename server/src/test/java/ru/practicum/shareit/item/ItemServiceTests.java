package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTests {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private Item itemToCreate;
    private User itemOwner;

    @BeforeEach
    void setUp() {
        itemOwner = new User(1, "Иван", "ivanov.ivan@yandex.ru");
        itemToCreate = new Item(null, "Фотоаппарат", "Nikon", true, itemOwner, null);
    }

    @Test
    void createNewItemTest() {
        assertThrows(NotFoundException.class, () -> itemService.addItem(itemOwner.getId(), itemToCreate, null));//Пользователя с таким id еще нет в БД
        UserResponseDto user = userService.addNewUser(itemOwner);
        assertDoesNotThrow(() -> itemService.addItem(user.getId(), itemToCreate, null));
        Item createdItem = itemService.addItem(user.getId(), itemToCreate, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemTest = query.setParameter("id", createdItem.getId())
                .getSingleResult();
        assertThat(createdItem.getId(), notNullValue());
        assertEquals(createdItem.getId(), itemTest.getId());
        assertEquals(createdItem.getName(), itemTest.getName());
    }

    @Test
    void getItemByIdTest() {
        UserResponseDto user = userService.addNewUser(itemOwner);
        assertThrows(NotFoundException.class, () -> itemService.getItemById(1, user.getId()));
        Item createdItem = itemService.addItem(user.getId(), itemToCreate, null);
        assertDoesNotThrow(() -> itemService.getItemById(createdItem.getId(), user.getId()));
        assertThrows(NotFoundException.class, () -> itemService.getItemById(2, user.getId()));
    }

    @Test
    void getItemsByCaptionTest() {
        UserResponseDto user = userService.addNewUser(itemOwner);
        Item secondItem = new Item(null, "Второй", "Nikon", true, UserMapper.toDomainModel(user), null);

        List<Item> noItemsList = itemService.getItemsByCaption("");
        assertEquals(0, noItemsList.size());
        itemService.addItem(user.getId(), itemToCreate, null);
        itemService.addItem(user.getId(), secondItem, null);

        List<Item> emptyCaptionList = itemService.getItemsByCaption("");
        assertEquals(0, emptyCaptionList.size());

        List<Item> secondCaptionList = itemService.getItemsByCaption("Второй");
        assertEquals(1, secondCaptionList.size());

        List<Item> nikonCaptionList = itemService.getItemsByCaption("Nikon");
        assertEquals(2, nikonCaptionList.size());
    }

    @Test
    void getUserItemsTest() {
        UserResponseDto user = userService.addNewUser(itemOwner);

        List<ItemResponseDto> items = itemService.getUserItems(user.getId());
        assertEquals(items.size(), 0);

        itemService.addItem(user.getId(), itemToCreate, null);
        items = itemService.getUserItems(user.getId());
        assertEquals(1, items.size());

        Item secondItem = new Item(null, "Второй", "Nikon", true, UserMapper.toDomainModel(user), null);
        itemService.addItem(user.getId(), secondItem, null);
        items = itemService.getUserItems(user.getId());
        assertEquals(2, items.size());

        User otherItemOwner = new User(2, "Гоша", "Ivanov@mail.ru");
        UserResponseDto user2 = userService.addNewUser(otherItemOwner);

        Item thirdItem = new Item(null, "Третий", "Nikon", true, UserMapper.toDomainModel(user2), null);
        itemService.addItem(user2.getId(), thirdItem, null);

        items = itemService.getUserItems(user.getId());
        assertEquals(2, items.size());
        items = itemService.getUserItems(user2.getId());
        assertEquals(1, items.size());
    }

    @Test
    void updateItemTest() {
        UserResponseDto user = userService.addNewUser(itemOwner);
        Item createdItem = itemService.addItem(user.getId(), itemToCreate, null);
        assertEquals(itemToCreate.getName(), createdItem.getName());
        Item copyItem = new Item(createdItem.getId(), "Другое имя", createdItem.getDescription(), createdItem.getAvailable(),
                createdItem.getOwner(), createdItem.getRequest());
        String oldItemName = createdItem.getName();
        Item updatedItem = itemService.updateItem(user.getId(), copyItem);
        assertNotEquals(oldItemName, updatedItem.getName());
        assertEquals(itemToCreate.getDescription(), updatedItem.getDescription());
    }
}
