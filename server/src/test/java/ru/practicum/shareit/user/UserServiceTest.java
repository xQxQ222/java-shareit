package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final EntityManager em;
    private final UserService userService;
    private User userToCreate;
    private UserResponseDto user;

    @BeforeEach
    void setUp() {
        userToCreate = new User(null, "Ivan", "ivanov.ivan@mail.ru");
        addNewUserToDB();
    }

    @Test
    void createUserTest() {
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User testUser = query.setParameter("id", user.getId())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertEquals(user.getId(), testUser.getId());
        assertEquals(user.getName(), testUser.getName());
        assertEquals(user.getEmail(), testUser.getEmail());

    }

    @Test
    void getAllUsersTest() {
        List<UserResponseDto> users = userService.getAllUsers();
        TypedQuery<Long> query = em.createQuery("Select count(*) from User u", Long.class);
        assertEquals(users.size(), query.getSingleResult());
    }

    @Test
    void getUserByIdTest() {
        User secondUser = new User(null, "second", "im_second@mail.ru");
        UserResponseDto secondUserResponse = userService.addNewUser(secondUser);
        UserResponseDto getUser = userService.getUserById(secondUserResponse.getId());
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User testUser = query.setParameter("id", secondUserResponse.getId())
                .getSingleResult();

        assertThat(secondUserResponse.getId(), notNullValue());
        assertEquals(getUser.getId(), testUser.getId());
        assertEquals(getUser.getName(), testUser.getName());
        assertEquals(getUser.getEmail(), testUser.getEmail());
    }

    @Test
    void deleteUserTest() {
        int userCount = userService.getAllUsers().size();
        userService.deleteUser(user.getId());
        int newUserCount = userService.getAllUsers().size();
        assertEquals(newUserCount, userCount - 1);
    }

    @Test
    void updateUserTest() {
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User testUserV1 = query.setParameter("id", user.getId())
                .getSingleResult();
        assertEquals(user.getName(), testUserV1.getName());
        User userToUpdate = new User(user.getId(), "New Name", user.getEmail());
        UserResponseDto updatedUser = userService.updateUser(userToUpdate);
        User testUserV2 = query.setParameter("id", user.getId())
                .getSingleResult();
        assertNotEquals(user.getName(), testUserV2.getName());
        assertEquals(user.getId(), updatedUser.getId());
    }

    private void addNewUserToDB() {
        user = userService.addNewUser(userToCreate);
    }
}
