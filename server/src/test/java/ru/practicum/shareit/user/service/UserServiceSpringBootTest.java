package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceSpringBootTest {
    @Autowired
    private UserServiceImpl userService;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        userDto = makeUserDto("Пётр", "some@email.com");
    }

    @Test
    void createUserTest() {
        UserDto result = userService.createUser(userDto);

        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
        assertThat(result.getName(), equalTo(userDto.getName()));
    }

    @Test
    void updateUserTest() {
        UserDto dto = makeUserDto("updated", "updated@mail.ru");
        userService.createUser(userDto);
        UserDto result = userService.updateUserById(1L, dto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getEmail(), equalTo(dto.getEmail()));
        assertThat(result.getName(), equalTo(dto.getName()));
    }

    @Test
    void deleteUserByIdTest() {
        UserDto dto = userService.createUser(userDto);

        assertThat(dto.getId(), equalTo(1L));
        userService.deleteUserById(1L);
        assertThrows(UserNotFoundException.class, () -> userService.getUserDtoById(1L));
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setName(name);

        return dto;
    }
}
