package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserMapperTest {
    User user = new User(1L, "name1", "mail1@mail.ru");
    User user2 = new User(2L, "name2", "mail2@mail.ru");
    UserDto userDto = new UserDto(1L, "namedto1", "maildto1@mail.ru");
    UserMapper mapper = new UserMapper();

    @Test
    void mapToUserDtoTest() {
        UserDto result = mapper.mapToUserDto(user);
        assertEquals(result.getName(), "name1");
    }

    @Test
    void mapToUserTest() {
        User result = mapper.mapToUser(userDto);
        assertEquals(result.getName(), "namedto1");
    }

    @Test
    void mapToListUserDtoTest() {
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        List<UserDto> result = mapper.mapToListUserDto(list);
        assertEquals(result.get(0).getName(), "name1");
        assertEquals(result.get(1).getName(), "name2");
    }
}
