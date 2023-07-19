package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(long id, UserDto user);

    void deleteUser(long id);

    User getUserById(long id);
}