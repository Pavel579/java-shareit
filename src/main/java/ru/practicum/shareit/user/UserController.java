package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userMapper.mapToUserDto(userService.createUser(userMapper.mapToUser(userDto)));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(userMapper::mapToUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.mapToUserDto(userService.getUserById(id));
    }

    @PatchMapping("/{id}")
    public UserDto updateUserById(@PathVariable Long id,
                                  @Validated(Update.class) @RequestBody UserDto userDto) {
        return userMapper.mapToUserDto(userService.updateUserById(id, userDto));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
