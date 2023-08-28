package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User not found")
public class UserNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public UserNotFoundException() {
        super();
    }

}
