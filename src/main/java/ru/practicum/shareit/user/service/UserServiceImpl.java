package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User request) {
        return userRepository.save(request);
    }

    @Override
    public User updateUser(long id, UserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        Optional.ofNullable(request.getName()).ifPresent(user::setName);
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}


