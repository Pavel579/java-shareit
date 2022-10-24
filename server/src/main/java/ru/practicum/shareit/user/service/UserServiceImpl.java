package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = repository.save(mapper.mapToUser(userDto));
        return mapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return mapper.mapToListUserDto(repository.findAll());
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return mapper.mapToUserDto(user);
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    @Transactional
    public UserDto updateUserById(Long id, UserDto userDto) {
        User userFromStorage = getUserById(id);
        BeanUtils.copyProperties(userDto, userFromStorage, getNullPropertyNames(userDto));
        User user = repository.save(userFromStorage);
        return mapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
