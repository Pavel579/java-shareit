package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private final UserMapper mapper = new UserMapper();
    private UserServiceImpl userService;
    private UserRepository userRepository;

    private User user;
    private User user2;
    private UserDto userDto;


    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, mapper);
        user = new User(1L, "name1", "email1@mail.ru");
        user2 = new User(1L, "namedto1", "email2@mail.ru");
        userDto = new UserDto(1L, "namedto1", "emaildto1@mail.ru");
    }

    @Test
    void getAllUsersTest() {
        List<User> users = new ArrayList<>(Collections.singletonList(user));
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> usersDto = userService.getAllUsers();
        assertNotNull(usersDto);
        assertEquals(usersDto.get(0).getName(), "name1");
        assertEquals(1, usersDto.size());
    }

    @Test
    void getUserDtoByIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        UserDto result = userService.getUserDtoById(1L);
        assertNotNull(result);
        assertEquals(result.getName(), "name1");
    }

    @Test
    void getUserDtoByWrongIdTest() {
        when(userRepository.findById(10L)).thenThrow(new UserNotFoundException("Пользователь не найден"));
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserDtoById(10L));
        UserNotFoundException ex2 = assertThrows(UserNotFoundException.class, () -> userService.getUserById(10L));
        assertEquals("Пользователь не найден", ex.getMessage());
        assertEquals("Пользователь не найден", ex2.getMessage());
    }

    @Test
    void createUserTest() {
        when(userRepository.save(user)).thenReturn(user);
        UserDto result = userService.createUser(userDto);
        assertNotNull(result);
        assertEquals(result.getName(), "name1");
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        User result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals(result.getName(), "name1");
    }

    @Test
    void updateUserByIdTest() {
        when(userRepository.save(user)).thenReturn(user2);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        UserDto result = userService.updateUserById(1L, userDto);
        assertNotNull(result);
        assertEquals(result.getName(), "namedto1");
    }
}
