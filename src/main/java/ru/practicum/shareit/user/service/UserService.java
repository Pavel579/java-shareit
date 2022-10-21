package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);

    List<UserDto> getAllUsers();

    User getUserById(Long id);

    UserDto getUserDtoById(Long id);

    UserDto updateUserById(Long id, UserDto userDto);

    void deleteUserById(Long id);

}
