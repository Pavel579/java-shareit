package ru.practicum.shareit.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        repository.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    public User updateUserById(Long id, UserDto userDto) {
        User userFromStorage = getUserById(id);
        BeanUtils.copyProperties(userDto, userFromStorage, getNullPropertyNames(userDto));
        return repository.save(userFromStorage);
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
