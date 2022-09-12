package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUserById(Long id, UserDto userDto);

    void deleteUserById(Long id);

}
