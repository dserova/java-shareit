package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final ModelMapper mapper;

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> mapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable(name = "id") Long userId) {
        User user = userService.getUserById(userId);
        return mapper.map(user, UserResponseDto.class);
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        User request = mapper.map(userRequestDto, User.class);
        User user = userService.createUser(request);
        return mapper.map(user, UserResponseDto.class);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable(name = "id") long userId, @RequestBody UserRequestDto userRequestDto) {
        User user = userService.updateUser(userId, userRequestDto);
        return mapper.map(user, UserResponseDto.class);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }
}
