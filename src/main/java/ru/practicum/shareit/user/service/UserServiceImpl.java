package ru.practicum.shareit.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailDuplicatedException;
import ru.practicum.shareit.exceptions.IncorrectIdException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Service
public class UserServiceImpl implements UserService {
    //private final UserStorage inMemoryUserStorage;
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user){
        repository.save(user);
        return user;
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User getUserById(Long id){
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    public User updateUserById(Long id, UserDto userDto){
        User userFromStorage = getUserById(id);
        BeanUtils.copyProperties(userDto, userFromStorage, getNullPropertyNames(userDto));
        return repository.save(userFromStorage);
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    /*@Override
    public User createUser(User user) {
        checkEmail(user.getEmail());
        return inMemoryUserStorage.createUser(user);
    }*/

    /*@Override
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }*/

    /*@Override
    public User getUserById(Long id) {
        checkUserId(id);
        return inMemoryUserStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }*/

    /*@Override
    public User updateUserById(Long id, UserDto userDto) {
        checkEmail(userDto.getEmail());
        User userFromStorage = getUserById(id);
        return inMemoryUserStorage.updateUserById(userDto, userFromStorage);
    }*/

    /*@Override
    public void deleteUserById(Long id) {
        getUserById(id);
        inMemoryUserStorage.deleteUserById(id);
    }*/

    private void checkUserId(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectIdException(String.format("Id пользователя <%s> некорректен", id));
        }
    }

    /*private void checkEmail(String mail) {
        for (User user : inMemoryUserStorage.getAllUsers()) {
            if (user.getEmail().equals(mail)) {
                throw new EmailDuplicatedException("Такой Email уже есть");
            }
        }
    }*/
}
