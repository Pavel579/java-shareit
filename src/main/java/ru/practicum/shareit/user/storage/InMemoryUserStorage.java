package ru.practicum.shareit.user.storage;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private static Long id = 0L;
    private final Map<Long, User> userStorage = new HashMap<>();

    public User createUser(User user) {
        user.setId(setIdToUser());
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    @Override
    public User updateUserById(UserDto userDto, User userFromStorage) {
        BeanUtils.copyProperties(userDto, userFromStorage, getNullPropertyNames(userDto));
        return userFromStorage;
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.remove(id);
    }

    private Long setIdToUser() {
        return ++id;
    }
}
