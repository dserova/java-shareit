package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final ModelMapper mapper;

    private final UserService service;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return service.getAllUsers().stream().map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable(name = "id") Long id) {
        User user = service.getUserById(id);
        return mapper.map(user, UserDto.class);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto request) {
        User userRequest = mapper.map(request, User.class);
        User user = service.createUser(userRequest);
        return mapper.map(user, UserDto.class);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody UserDto request) {
        User user = service.updateUser(id, request);
        return mapper.map(user, UserDto.class);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable(name = "id") Long id) {
        service.deleteUser(id);
    }
}
