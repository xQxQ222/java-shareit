package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {

    private final JacksonTester<UserRequestDto> userDtoJacksonTester;
    private final JacksonTester<UserResponseDto> userResponseDtoJacksonTester;

    @Test
    void testUserRequestDto() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("Иван", "ivan.ivanov@mail.ru");
        JsonContent<UserRequestDto> result = userDtoJacksonTester.write(userRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Иван");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ivan.ivanov@mail.ru");
    }

    @Test
    void testUserResponseDto() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto(1, "Иван", "ivan.ivanov@mail.ru");
        JsonContent<UserResponseDto> result = userResponseDtoJacksonTester.write(userResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Иван");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ivan.ivanov@mail.ru");
    }
}
