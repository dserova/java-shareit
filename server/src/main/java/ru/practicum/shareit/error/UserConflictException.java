package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User with same email exists")
public class UserConflictException extends RuntimeException {
    HttpStatus httpStatus;

    public UserConflictException() {
        super();
    }

}
