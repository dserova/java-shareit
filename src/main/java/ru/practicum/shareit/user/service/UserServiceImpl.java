package ru.practicum.shareit.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ResourceNotFoundException;
import ru.practicum.shareit.share.RegexpValidation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        String email = user.getEmail();
        RegexpValidation re = new RegexpValidation();
        if (!email.matches(re.checkEmailPattern())) {
            throw new ResourceNotFoundException("Email not valid", HttpStatus.BAD_REQUEST);
        }
        User find = userRepository.findByEmail(email);
        if (find != null) {
            throw new ResourceNotFoundException("Email not allowed", HttpStatus.CONFLICT);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, UserDto userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", HttpStatus.NOT_FOUND));

        String email = userRequest.getEmail();
        String name = userRequest.getName();

        if (email != null) {
            User find = userRepository.findByEmail(email);
            if (find != null) {
                if (id != 0 && id != find.getId()) {
                    throw new ResourceNotFoundException("User with same email exists", HttpStatus.CONFLICT);
                }
            }

            RegexpValidation re = new RegexpValidation();
            if (!email.matches(re.checkEmailPattern())) {
                throw new ResourceNotFoundException("Email not valid", HttpStatus.BAD_REQUEST);
            }
            user.setEmail(userRequest.getEmail());
        }

        if (name != null) {
            user.setName(userRequest.getName());
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", HttpStatus.NOT_FOUND));

        userRepository.delete(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
    }
}


