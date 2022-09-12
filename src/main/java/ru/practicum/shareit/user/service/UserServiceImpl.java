package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailDuplicatedException;
import ru.practicum.shareit.exceptions.IncorrectIdException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserServiceImpl(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public User createUser(User user) {
        checkEmail(user.getEmail());
        return inMemoryUserStorage.createUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        checkUserId(id);
        return inMemoryUserStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public User updateUserById(Long id, UserDto userDto) {
        checkEmail(userDto.getEmail());
        User userFromStorage = getUserById(id);
        return inMemoryUserStorage.updateUserById(userDto, userFromStorage);
    }

    @Override
    public void deleteUserById(Long id) {
        getUserById(id);
        inMemoryUserStorage.deleteUserById(id);
    }

    private void checkUserId(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectIdException(String.format("Id пользователя <%s> некорректен", id));
        }
    }

    private void checkEmail(String mail) {
        for (User user : inMemoryUserStorage.getAllUsers()) {
            if (user.getEmail().equals(mail)) {
                throw new EmailDuplicatedException("Такой Email уже есть");
            }
        }
    }
}
