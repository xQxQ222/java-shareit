package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;


    private UserResponseDto userResponseDto = new UserResponseDto(
            1,
            "John",
            "john.doe@mail.com"
    );

    @Test
    public void registerNewUserTest() throws Exception {
        when(userService.addNewUser(any(User.class)))
                .thenReturn(userResponseDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userResponseDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(userResponseDto.getId())))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail())));

        verify(userService, times(1))
                .addNewUser(any(User.class));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(anyInt()))
                .thenReturn(userResponseDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponseDto.getId()))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail())));

        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(userResponseDto));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userService.updateUser(any(User.class)))
                .thenReturn(userResponseDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userResponseDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userResponseDto.getId())))
                .andExpect(jsonPath("$.name", Matchers.is(userResponseDto.getName())))
                .andExpect(jsonPath("$.email", Matchers.is(userResponseDto.getEmail())));

        verify(userService, times(1))
                .updateUser(any(User.class));
    }

    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService)
                .deleteUser(anyInt());

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1))
                .deleteUser(anyInt());
    }
}
