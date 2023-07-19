package ru.practicum.shareit.user.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> modelMapper.map(user, User.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);
        return modelMapper.map(user, User.class);
    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        User userRequest = modelMapper.map(userDto, User.class);
        User user = userService.createUser(userRequest);
        return modelMapper.map(user, User.class);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }
}
